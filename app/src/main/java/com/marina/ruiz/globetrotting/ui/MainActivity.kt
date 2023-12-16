package com.marina.ruiz.globetrotting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_area) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        hideNavController()
    }

    private fun hideNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.bookingCreationFormFragment
                || destination.id == R.id.destinationDetailFragment
            ) {
                binding.navigation.visibility = View.GONE
            } else {
                binding.navigation.visibility = View.VISIBLE
            }
        }
    }
}