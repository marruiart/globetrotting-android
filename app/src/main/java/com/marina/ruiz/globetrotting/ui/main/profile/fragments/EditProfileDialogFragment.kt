package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.Manifest.permission
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.databinding.FragmentEditProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.model.Profile
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm
import java.io.File

interface EditProfileDialogFragmentListener {
    fun onAccept(data: Profile)
    fun onCancel()
}

class EditProfileDialogFragment(
    private val callback: EditProfileDialogFragmentListener, private val profile: Profile
) : FullScreenDialogFragment(R.layout.fragment_edit_profile) {
    private val PADDING = 100
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var form: ProfileForm
    private lateinit var photoFile: File
    var image_uri: Uri? = null

    companion object {
        private const val CAMERA_REQUEST_CODE = 1001
        private const val GALLERY_REQUEST_CODE = 1002
        private const val IMAGE_CAPTURE_REQUEST_CODE = 1003
        private const val SELECT_PICTURE_REQUEST_CODE = 1004
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets(view)
        initForm()
        bindView()
        initListeners()
    }

    private fun initForm() {
        form = ProfileForm(
            tilName = binding.tilFormName,
            tilSurname = binding.tilFormSurname,
            tilNickname = binding.tilFormNickname,
            positiveBtn = binding.btnAcceptEditProfile,
            neutralBtn = binding.btnCancelEditProfile
        )
    }

    private fun bindView() {
        form.tilName.setText(profile.name)
        form.tilSurname.setText(profile.surname)
        form.tilNickname.setText(profile.nickname)
    }

    private fun setWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + PADDING,
                systemBars.top + PADDING,
                systemBars.right + PADDING,
                systemBars.bottom + PADDING
            )
            insets
        }
    }

    private fun initListeners() {
        binding.btnAcceptEditProfile.setOnClickListener {
            val profile = Profile(
                name = form.tilName.text.toString(),
                surname = form.tilSurname.text.toString(),
                nickname = form.tilNickname.text.toString()
            )
            callback.onAccept(profile)
        }
        binding.btnCancelEditProfile.setOnClickListener {
            callback.onCancel()
        }
        binding.btnChangeAvatarProfile.setOnClickListener {
            val CAMERA = false
            if (CAMERA) {
                checkPermissions(
                    permission.CAMERA, CAMERA_REQUEST_CODE, ::openCamera, ::requestPermission
                )
            } else {
                checkPermissions(
                    permission.READ_EXTERNAL_STORAGE,
                    GALLERY_REQUEST_CODE,
                    ::openGallery,
                    ::requestPermission
                )
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraARL.launch(cameraIntent)
    }

    var cameraARL: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            binding.ivAvatarEditProfile.setImageURI(image_uri)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.setType("image/*")
        galleryARL.launch(galleryIntent)
    }

    var galleryARL: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            image_uri = activityResult.data?.data
            binding.ivAvatarEditProfile.setImageURI(image_uri)
        }
    }

    private fun requestPermission(permission: String, code: Int) {
        if (shouldRequestPermissionRationale(permission)) {
            Toast.makeText(
                requireActivity(), "Dale permisos illo, que ya te los pedí", Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), code)
        }
    }

    private fun shouldRequestPermissionRationale(permission: String): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(), permission
        )

    private fun hasPermission(permission: String): Boolean = checkSelfPermission(
        requireActivity(), permission
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkPermissions(
        permission: String,
        code: Int,
        onProvided: () -> Unit,
        onDenied: (permission: String, code: Int) -> Unit
    ) = if (hasPermission(permission)) onProvided() else onDenied(permission, code)
}