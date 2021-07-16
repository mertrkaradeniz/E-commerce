package com.example.e_commerce.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
//    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.productsFragment,
//                R.id.dashboardFragment,
//                R.id.labelFragment,
//                R.id.profileFragment
//            )
//        )
        binding.bottomNavigationView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }



}