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

        /**
         * Creates an Intent to start MainActivity.
         *
         * @param context The context used to create the Intent.
         * @return The Intent to start MainActivity.
         */
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

    /**
     * Gets the height of the action bar.
     *
     * @return The height of the action bar in pixels.
     */
    private fun getActionBarSize(): Int {
        val typedValue = TypedValue()
        val hasSize = theme.resolveAttribute(actionBarSize, typedValue, true)
        return if (hasSize) TypedValue.complexToDimensionPixelSize(
            typedValue.data, resources.displayMetrics
        )
        else 0
    }

    /**
     * Sets system window insets for the app bar layout.
     */
    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    /**
     * Initializes the navigation controller.
     */
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

    /**
     * Initializes listeners for UI components.
     */
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

    /**
     * Initializes observers for ViewModel LiveData.
     */
    private fun initObservers() {
        mainVM.logout.observe(this) { logout ->
            if (logout) {
                navigateToLogin()
            }
        }
    }


    /**
     * Navigates to the login screen.
     */
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