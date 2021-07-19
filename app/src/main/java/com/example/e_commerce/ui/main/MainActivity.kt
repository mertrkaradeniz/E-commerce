package com.example.e_commerce.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun showProductIndicator() {
        setDefaultVisibilityOfIndicator()
        binding.indicatorProduct.visibility = View.VISIBLE
    }

    fun showDashboardIndicator() {
        setDefaultVisibilityOfIndicator()
        binding.indicatorDashboard.visibility = View.VISIBLE
    }

    fun showLabelIndicator() {
        setDefaultVisibilityOfIndicator()
        binding.indicatorLabel.visibility = View.VISIBLE
    }

    fun showProfileIndicator() {
        setDefaultVisibilityOfIndicator()
        binding.indicatorProfile.visibility = View.VISIBLE
    }

    fun hideBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun setDefaultVisibilityOfIndicator() {
        binding.indicatorProduct.visibility = View.INVISIBLE
        binding.indicatorDashboard.visibility = View.INVISIBLE
        binding.indicatorLabel.visibility = View.INVISIBLE
        binding.indicatorProfile.visibility = View.INVISIBLE
    }

}