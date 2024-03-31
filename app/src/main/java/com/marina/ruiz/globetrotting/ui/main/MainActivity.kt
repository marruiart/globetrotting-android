package com.marina.ruiz.globetrotting.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.ActivityMainBinding
import com.marina.ruiz.globetrotting.ui.auth.AuthActivity
import com.marina.ruiz.globetrotting.ui.main.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainVM: MainViewModel by viewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG MAIN_ACTIVITY"

        fun create(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()
        initNavController()
        initUI()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun initNavController() {
        navController = getNavHostFragment().navController
        binding.navigation.setupWithNavController(navController)
    }

    private fun getNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragment_main_area) as NavHostFragment

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.mainTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_my_profile -> {
                    val intent = ProfileActivity.create(this)
                    intent.putExtra("user", mainVM.user.value)
                    startActivity(intent)
                    true
                }

                R.id.menu_logout -> {
                    mainVM.deleteUser()
                    true
                }

                else -> false
            }
        }
    }

    private fun initObservers() {
        mainVM.logout.observe(this) { logout ->
            if (logout) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = AuthActivity.create(this)
        intent.putExtra("LOGOUT", true)
        startActivity(intent)
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity Destroy")
    }
}