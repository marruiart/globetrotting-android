package com.marina.ruiz.globetrotting.ui.main.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.PermissionsService
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.databinding.ActivityProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragment
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragmentListener
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), EditProfileDialogFragmentListener {
    private lateinit var binding: ActivityProfileBinding
    private val profileVM: ProfileViewModel by viewModels()
    private lateinit var systemBars: Insets
    private lateinit var dialog: EditProfileDialogFragment
    private var _user: User? = null
    private var uriImage: Uri? = null

    companion object {
        private const val TAG = "GLOB_DEBUG PROFILE_ACTIVITY"

        fun create(context: Context): Intent = Intent(context, ProfileActivity::class.java)
    }

    private val filePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { enabled ->
            if (enabled) {
                //updateAvatar()
            } else {
                PermissionsService.requirePermissionsDialog(this)
            }
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
        //filePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    private fun bindAvatarImage(user: User) {
        Glide.with(this).load(user.avatar).centerCrop().placeholder(R.drawable.default_avatar)
            .into(binding.ivAvatarProfile)
    }

    private fun bindView() {
        _user?.let {
            with(binding) {
                tvName.text = getString(R.string.full_name, it.name, it.surname)
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

    override fun onAccept(profile: ProfileForm, avatar: Uri?) {
        profileVM.editProfile(this, profile, avatar)
        dialog.dismiss()
    }

    override fun onCancel() {
        Log.d(TAG, "Cancel")
        dialog.dismiss()
    }

}