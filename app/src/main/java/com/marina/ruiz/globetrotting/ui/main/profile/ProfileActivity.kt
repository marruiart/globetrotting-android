package com.marina.ruiz.globetrotting.ui.main.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.network.firebase.StorageFileListeners
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.databinding.ActivityProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragment
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragmentListener
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), EditProfileDialogFragmentListener,
    StorageFileListeners {
    private lateinit var binding: ActivityProfileBinding
    private val profileVM: ProfileViewModel by viewModels()
    private lateinit var systemBars: Insets
    private lateinit var dialog: EditProfileDialogFragment
    private var _user: User? = null

    companion object {
        private const val TAG = "GLOB_DEBUG PROFILE_ACTIVITY"

        fun create(context: Context): Intent = Intent(context, ProfileActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileVM.user.observe(this) { user ->
            _user = user
            bindView()
        }
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()
        initUI()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_app_bar_layout)) { v, insets ->
            systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun initUI() {
        bindView()
        initListeners()
    }

    /**
     * Binds the user's avatar image to the ImageView.
     * @param user The user whose avatar image is to be bound.
     */
    private fun bindAvatarImage(user: User) {
        Glide.with(this).load(user.avatar).centerCrop().placeholder(R.drawable.default_avatar)
            .into(binding.ivAvatarProfile)
    }

    /**
     * Binds the user's avatar image and other profile information to the UI elements.
     */
    private fun bindView() {
        _user?.let {
            with(binding) {
                val name = if (it.name != null) getString(R.string.full_name, it.name, it.surname) else it.username
                tvName.text = name
                tvNickname.text = it.nickname
                tvUsername.text = it.username
                tvEmail.text = it.email
            }
            bindAvatarImage(it)
        }
    }


    private fun initListeners() {
        binding.btnEdit.setOnClickListener { showDialog() }

        binding.profileTopAppBar.setNavigationOnClickListener { this.finish() }
    }

    /**
     * Shows the edit profile dialog.
     */
    private fun showDialog() {
        _user?.let { user ->
            val profile = ProfileForm(
                user.username,
                user.email,
                user.name ?: "",
                user.surname ?: "",
                user.nickname,
                user.avatar
            )
            dialog = EditProfileDialogFragment(this, profile)
            dialog.show(supportFragmentManager, "EditProfileFragment")
        }
    }

    override fun onAccept(
        profile: ProfileForm, avatar: Uri?, removeImage: Boolean, nameChanged: Boolean
    ) {
        val clientName: String? = if (nameChanged) {
            getString(R.string.full_name, profile.name, profile.surname)
        } else {
            null
        }
        if (avatar == null && removeImage || avatar != null) {
            profileVM.uploadAvatar(avatar, profile.toProfilePayload(), clientName, this)
        } else {
            profileVM.editProfile(profile.toProfilePayload(), clientName)
        }
        dialog.dismiss()
    }

    override fun onCancel() {
        Log.d(TAG, "Cancel")
        dialog.dismiss()
    }

    override fun onUploadSuccess(downloadUri: Uri?, profile: ProfilePayload, clientName: String?) {
        if (downloadUri == null) {
            profile.avatar = null
        } else if (downloadUri.toString() != profile.avatar) {
            profile.avatar = downloadUri.toString()
            Log.d(TAG, "New URL: $downloadUri")
            Toast.makeText(this, "Foto actualizada", Toast.LENGTH_SHORT).show()
        }
        profileVM.editProfile(profile, clientName)
    }

    override fun onUploadFailed(exception: Exception) {
        Log.e("GLOB_DEBUG EDIT_PROFILE_USECASE", exception.message ?: "Upload failed")
        Toast.makeText(this, "Error al actualizar la foto de perfil", Toast.LENGTH_LONG).show()
    }


}