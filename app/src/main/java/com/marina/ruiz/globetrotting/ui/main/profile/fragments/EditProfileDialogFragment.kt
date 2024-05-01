package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.core.dialog.PhotoSourcePickerBottomSheet
import com.marina.ruiz.globetrotting.core.dialog.PhotoSourcePickerListener
import com.marina.ruiz.globetrotting.databinding.FragmentEditProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm

interface EditProfileDialogFragmentListener {
    fun onAccept(profile: ProfileForm, avatar: Uri?)
    fun onCancel()
}

class EditProfileDialogFragment(
    private val callback: EditProfileDialogFragmentListener, private val profile: ProfileForm
) : FullScreenDialogFragment(R.layout.fragment_edit_profile), PhotoSourcePickerListener {
    private val PADDING = 100
    private lateinit var binding: FragmentEditProfileBinding
    var imageUri: Uri? = null
    val sourcePicker = PhotoSourcePickerBottomSheet(this)

    companion object {
        private const val TAG = "GLOB_DEBUG EDIT_PROFILE_DIALOG_FRAGMENT"
    }

    private val pickImage = registerForActivityResult(GetContent()) { uri ->
        uri?.let {
            Log.d(TAG, "picking image...")
            imageUri = uri
            binding.ivAvatarEditProfile.setImageURI(uri)
        }
    }

    private val takePicture = registerForActivityResult(TakePicture()) { enabled ->
        if (enabled) {
            binding.ivAvatarEditProfile.setImageURI(imageUri)
        }
    }

    private val cameraPermission = registerForActivityResult(RequestPermission()) { enabled ->
        if (enabled) {
            openCamera()
        } else {
            requirePermissionsDialog()
        }
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
        bindView()
        initListeners()
    }

    private fun bindView() {
        binding.tvUsername.text = profile.username
        binding.tvEmail.text = profile.email
        binding.tilFormName.setText(profile.name)
        binding.tilFormSurname.setText(profile.surname)
        binding.tilFormNickname.setText(profile.nickname)
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
            val profileForm = ProfileForm(
                username = profile.username,
                email = profile.email,
                name = binding.tilFormName.text.toString(),
                surname = binding.tilFormSurname.text.toString(),
                nickname = binding.tilFormNickname.text.toString()
            )
            callback.onAccept(profileForm, imageUri)
        }
        binding.btnCancelEditProfile.setOnClickListener {
            callback.onCancel()
        }
        binding.btnChangeAvatarProfile.setOnClickListener {
            sourcePicker.show(
                requireActivity().supportFragmentManager, PhotoSourcePickerBottomSheet.TAG
            )
        }
        binding.btnRemoveAvatarProfile.setOnClickListener {
            removePictureDialog()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        takePicture.launch(imageUri)
    }

    private fun openGallery() {
        pickImage.launch("image/*")
    }


    private fun openAppSettings() {
        val intent = Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireContext().applicationContext.packageName}")
        )
        startActivity(intent)
    }

    private fun removePicture() {
        binding.ivAvatarEditProfile.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.default_avatar, null)
        )
    }

    private fun removePictureDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Eliminar foto")
            .setMessage("¿Desea eliminar la foto de perfil?").setNeutralButton("No") { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton("Sí") { dialog, _ ->
                removePicture()
                dialog.dismiss()
            }.show()
    }

    private fun requirePermissionsDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Faltan permisos")
            .setMessage("No se han dado permisos, ¿desea abrir la configuración de la aplicación para concederlos?")
            .setNeutralButton("No, gracias") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    requireContext(), "No se ha podido abrir la cámara.", Toast.LENGTH_SHORT
                ).show()
            }.setPositiveButton("Vale") { dialog, _ ->
                openAppSettings()
                dialog.dismiss()
            }.show()
    }

    override fun onCameraAction() = cameraPermission.launch(Manifest.permission.CAMERA)
    override fun onGalleryAction() = openGallery()

}