package com.marina.ruiz.globetrotting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.ActivityMainBinding
import com.marina.ruiz.globetrotting.ui.auth.AuthActivity
import com.marina.ruiz.globetrotting.ui.auth.AuthViewModel
import com.marina.ruiz.globetrotting.ui.home.HomeFragmentDirections
import com.marina.ruiz.globetrotting.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        fun create(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setWindowInsets()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_area) as NavHostFragment
        val navView: BottomNavigationView = binding.navigation
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.mainTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_my_profile -> {
                    startActivity(ProfileActivity.create(this))
                    true
                }

                R.id.menu_logout -> {
                    Log.d("MENU", "logout")
                    authViewModel.onLogout()
                    true
                }

                else -> false
            }
        }
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initObservers() {

        authViewModel.navigateToHome.observe(this) { isLogged ->
            if (!isLogged) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(AuthActivity.create(this))
        this.finish()
    }
}