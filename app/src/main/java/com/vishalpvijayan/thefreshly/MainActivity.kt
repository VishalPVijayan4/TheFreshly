package com.vishalpvijayan.thefreshly

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.vishalpvijayan.thefreshly.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splash -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                R.id.login -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                R.id.forgotpassword -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }
                R.id.createccount -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                else -> {
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }
}