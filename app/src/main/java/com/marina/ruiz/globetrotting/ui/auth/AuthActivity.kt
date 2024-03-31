package com.marina.ruiz.globetrotting.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.NavHostFragment
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.ActivityAuthBinding
import com.marina.ruiz.globetrotting.ui.auth.viewmodel.AuthViewModel
import com.marina.ruiz.globetrotting.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val authVM: AuthViewModel by viewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG AUTH_ACTIVITY"

        fun create(context: Context): Intent =
            Intent(context, AuthActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoginState()
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        setWindowInsets()
        getNavHostFragment()
    }

    private fun getNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragment_auth_area) as NavHostFragment

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_auth)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initObservers() {
        authVM.allowAccess.distinctUntilChanged().observe(this) { navigate ->
            if (navigate) {
                Log.d(TAG, "Navigate home")
                navigateHome()
            }
        }
    }

    private fun checkLoginState() {
        val logout = intent.getBooleanExtra("LOGOUT", false)
        Log.d(TAG, "Logout: ${logout}")
        if (logout) {
            authVM.onLogout()
            return
        }
    }

    private fun navigateHome() {
        startActivity(MainActivity.create(this))
        Log.i(TAG, "FINISH")
        this.finish()
    }
}