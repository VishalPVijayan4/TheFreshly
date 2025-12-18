package com.vishalpvijayan.thefreshly

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.CurrentLocationRequest
import com.vishalpvijayan.thefreshly.databinding.ActivityMainBinding
import com.vishalpvijayan.thefreshly.presentation.vm.LoginVM
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.jar.Manifest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val toolbarViewModel: ToolbarViewModel by viewModels()
    private val loginViewModel: LoginVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)







        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                toolbarViewModel.toolbarTitle.collect { title ->
                    binding.customToolbar.findViewById<TextView>(R.id.toolbarTitle).text = title
                    binding.threeDotButton.setOnClickListener {
                        navController.navigate(R.id.action_dashboard_to_notificationFragment)
                    }
                    binding.ivBack.setOnClickListener {
                        navController.popBackStack()
                    }
                }
            }
        }

        // Observe Toolbar Subtitle (Optional)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                toolbarViewModel.toolbarSubTitle.collect { subTitle ->
                    binding.customToolbar.findViewById<TextView>(R.id.toolbarSubTitle).text = subTitle

                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.notificationFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                }
                R.id.profile -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                }
                R.id.dashboard -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.VISIBLE
                    binding.ivBack.visibility = View.GONE
                }

                R.id.productDetails -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.ivBack.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.VISIBLE
                }
                R.id.onboarding -> {
                    binding.cart.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }
                R.id.splash -> {
                    binding.cart.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                R.id.login -> {
                    binding.cart.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                R.id.forgotpassword -> {
                    binding.cart.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }
                R.id.createccount -> {
                    binding.cart.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.GONE
                }

                else -> {
                    binding.cart.visibility = View.VISIBLE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }


}