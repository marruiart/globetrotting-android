package com.marina.ruiz.globetrotting.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.databinding.ActivityProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragment
import com.marina.ruiz.globetrotting.ui.main.profile.fragments.EditProfileDialogFragmentListener

class ProfileActivity : AppCompatActivity(), EditProfileDialogFragmentListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var systemBars: Insets
    private lateinit var dialog: EditProfileDialogFragment
    private var user: User? = null

    companion object {
        private const val TAG = "GLOB_DEBUG PROFILE_ACTIVITY"
        fun create(context: Context): Intent =
            Intent(context, ProfileActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("user")
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
        initListeners()
    }

    private fun initListeners() {
        binding.btnEdit.setOnClickListener { showDialog() }

        binding.profileTopAppBar.setNavigationOnClickListener { this.finish() }
        user?.let {
            with(binding) {
                tvName.text = getString(R.string.full_name, it.name, it.surname)
                tvNickname.text = it.nickname
                tvUsername.text = it.username
                tvEmail.text = it.email
            }
        }
    }

    private fun showDialog() {
        dialog = EditProfileDialogFragment(this)
        dialog.show(supportFragmentManager, "EditProfileFragment")
    }

    override fun onAccept(data: String) {
        Log.d(TAG, "Accept " + data)
        dialog.dismiss()
    }

    override fun onCancel() {
        Log.d(TAG, "Cancel")
        dialog.dismiss()
    }

}