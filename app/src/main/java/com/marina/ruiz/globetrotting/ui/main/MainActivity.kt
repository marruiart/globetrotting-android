package com.marina.ruiz.globetrotting.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.R.attr.actionBarSize
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        fun create(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity")
        mainVM.registering = intent.getBooleanExtra("REGISTERING", false)
        Log.i(TAG, "registering: ${mainVM.registering}")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainVM.actionBarSize = getActionBarSize()
        setWindowInsets()
        initNavController()
        initUI()
    }

    private fun getActionBarSize(): Int {
        val typedValue = TypedValue()
        val hasSize = theme.resolveAttribute(actionBarSize, typedValue, true)
        return if (hasSize) TypedValue.complexToDimensionPixelSize(
            typedValue.data, resources.displayMetrics
        )
        else 0
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_area) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavView: BottomNavigationView = findViewById(R.id.bnv_navigation)
        bottomNavView.setupWithNavController(navController)
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.mtMainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_my_profile -> {
                    val intent = ProfileActivity.create(this)
                    intent.putExtra("user", mainVM.user.value)
                    startActivity(intent)
                    true
                }

                R.id.menu_logout -> {
                    mainVM.eraseDatabase()
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
        Log.d(TAG, "Navigating to login...")
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