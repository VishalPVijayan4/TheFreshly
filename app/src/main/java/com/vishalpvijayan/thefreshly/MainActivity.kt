package com.vishalpvijayan.thefreshly

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.razorpay.PaymentData
import com.vishalpvijayan.thefreshly.databinding.ActivityMainBinding
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), com.razorpay.PaymentResultWithDataListener,
    com.razorpay.ExternalWalletListener {

    private lateinit var binding: ActivityMainBinding
    private val toolbarViewModel: ToolbarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController

        binding.bottomNavView.setOnItemReselectedListener { }
        binding.bottomNavView.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id == item.itemId) {
                return@setOnItemSelectedListener true
            }
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.dashboard, false, true)
                .build()
            navController.navigate(item.itemId, null, navOptions)
            true
        }

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.threeDotButton.setOnClickListener {
            if (navController.currentDestination?.id == R.id.dashboard) {
                navController.navigate(R.id.action_dashboard_to_notificationFragment)
            }
        }
        binding.ivBack.setOnClickListener { navController.navigateUp() }

        observeToolbar()
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == R.id.dashboard && controller.graph.startDestinationId != R.id.dashboard) {
                // Once authentication is complete, Dashboard is the real start destination for
                // NavigationUI's saved top-level back stacks (Splash is only a routing screen).
                controller.graph.setStartDestination(R.id.dashboard)
            }
            updateNavigationChrome(destination.id)
            if (destination.id in TOP_LEVEL_DESTINATIONS && binding.bottomNavView.selectedItemId != destination.id) {
                binding.bottomNavView.menu.findItem(destination.id)?.isChecked = true
            }
        }
    }

    private fun observeToolbar() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    toolbarViewModel.toolbarTitle.collect { title ->
                        binding.customToolbar.findViewById<TextView>(R.id.toolbarTitle).text = title
                    }
                }
                launch {
                    toolbarViewModel.toolbarSubTitle.collect { subtitle ->
                        binding.customToolbar.findViewById<TextView>(R.id.toolbarSubTitle).text = subtitle
                    }
                }
            }
        }
    }

    private fun updateNavigationChrome(destinationId: Int) {
        val fullscreenDestinations = setOf(
            R.id.onboarding,
            R.id.splash,
            R.id.login,
            R.id.forgotpassword,
            R.id.createccount,
            R.id.mapFragment,
            R.id.payment
        )
        val toolbarHiddenDestinations = fullscreenDestinations + setOf(R.id.dashboard, R.id.product)

        binding.bottomNavView.isVisible = destinationId in TOP_LEVEL_DESTINATIONS
        binding.customToolbar.isVisible = destinationId !in toolbarHiddenDestinations
        binding.ivBack.isVisible = when {
            destinationId == R.id.SettingsFragment -> false
            destinationId in TOP_LEVEL_DESTINATIONS -> false
            destinationId in fullscreenDestinations -> false
            destinationId == R.id.orderSuccessFragment -> false
            else -> true
        }
        binding.threeDotButton.isVisible = destinationId == R.id.dashboard
    }

    fun startRazorpayPayment(options: org.json.JSONObject) {
        val checkout = com.razorpay.Checkout()
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY_ID)
        checkout.open(this, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        com.razorpay.Checkout.handleActivityResult(this, requestCode, resultCode, data, this, this)
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Log.d("MainActivity", "Payment successful")
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.e("MainActivity", "Payment error: code=$p0 response=$p1")
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        Log.d("MainActivity", "External wallet selected: $p0")
    }

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.dashboard, R.id.product, R.id.SettingsFragment)
    }

}
