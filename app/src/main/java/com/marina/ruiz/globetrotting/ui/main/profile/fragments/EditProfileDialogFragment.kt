package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.core.dialog.PhotoSourcePickerBottomSheet
import com.marina.ruiz.globetrotting.core.dialog.PhotoSourcePickerListener
import com.marina.ruiz.globetrotting.data.repository.PermissionsService
import com.marina.ruiz.globetrotting.databinding.FragmentEditProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm

/**
 * Interface for handling events in the edit profile dialog.
 */
interface EditProfileDialogFragmentListener {
    /**
     * Called when the accept button is clicked in the edit profile dialog.
     * @param profile The updated profile form.
     * @param avatar The updated avatar image URI.
     * @param removeImage A flag indicating whether to remove the current avatar image.
     */
    fun onAccept(profile: ProfileForm, avatar: Uri?, removeImage: Boolean, nameChanged: Boolean)
    /**
     * Called when the cancel button is clicked in the edit profile dialog.
     */
    fun onCancel()
}

/**
 * Dialog fragment for editing the user profile.
 *
 * @param callback The listener for dialog events.
 * @param profile The profile form to be edited.
 */
class EditProfileDialogFragment(
    private val callback: EditProfileDialogFragmentListener, private val profile: ProfileForm
) : FullScreenDialogFragment(R.layout.fragment_edit_profile), PhotoSourcePickerListener {
    private lateinit var binding: FragmentEditProfileBinding
    private var removeImage = false
    var imageUri: Uri? = null
    val sourcePicker = PhotoSourcePickerBottomSheet(this)

    companion object {
        private const val TAG = "GLOB_DEBUG EDIT_PROFILE_DIALOG_FRAGMENT"
        private const val PADDING = 100
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
            PermissionsService.requirePermissionsDialog(requireContext())
        }
    }

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Called immediately after {@link #onCreateView} has returned.
     *
     * @param view The View returned by {@link #onCreateView}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets(view)
        bindView()
        initListeners()
    }

    /**
     * Binds the avatar image to the view.
     */
    private fun bindAvatarImage() {
        Glide.with(this).load(profile.avatar).centerCrop().placeholder(R.drawable.default_avatar)
            .into(binding.ivAvatarEditProfile)
    }

    /**
     * Binds the profile data to the view.
     */
    private fun bindView() {
        binding.tvUsername.text = profile.username
        binding.tvEmail.text = profile.email
        binding.tilFormName.setText(profile.name)
        binding.tilFormSurname.setText(profile.surname)
        binding.tilFormNickname.setText(profile.nickname)
        bindAvatarImage()
    }

    /**
     * Sets the window insets for the view.
     *
     * @param view The view to set the insets for.
     */
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
                nickname = binding.tilFormNickname.text.toString(),
                avatar = profile.avatar
            )
            val nameChanged =
                profileForm.name != profile.name || profileForm.surname != profile.surname
            callback.onAccept(profileForm, imageUri, removeImage, nameChanged)
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


    /**
     * Opens the camera to take a picture.
     */
    private fun openCamera() {
        val values = ContentValues()
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        takePicture.launch(imageUri)
    }

    /**
     * Opens the gallery to pick an image.
     */
    private fun openGallery() {
        pickImage.launch("image/*")
    }

    /**
     * Removes the current picture and sets the default avatar.
     */
    private fun removePicture() {
        binding.ivAvatarEditProfile.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.default_avatar, null)
        )
        removeImage = true
    }

    /**
     * Shows a dialog to confirm the removal of the current picture.
     */
    private fun removePictureDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Eliminar foto")
            .setMessage("¿Desea eliminar la foto de perfil?").setNeutralButton("No") { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton("Sí") { dialog, _ ->
                removePicture()
                dialog.dismiss()
            }.show()
    }

    override fun onCameraAction() = cameraPermission.launch(Manifest.permission.CAMERA)
    override fun onGalleryAction() = openGallery()

}