package com.vishalpvijayan.thefreshly

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        // NavigationUI provides singleTop navigation, state restoration, and correct back-stack
        // handling for each top-level destination. Do not replace its item listener with direct navigate().
        binding.bottomNavView.setupWithNavController(navController)

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
        val topLevelDestinations = setOf(R.id.dashboard, R.id.product, R.id.SettingsFragment)
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

        binding.bottomNavView.visibility =
            if (destinationId in topLevelDestinations) View.VISIBLE else View.GONE
        binding.customToolbar.visibility =
            if (destinationId in toolbarHiddenDestinations) View.GONE else View.VISIBLE
        binding.ivBack.visibility = when {
            destinationId == R.id.SettingsFragment -> View.GONE
            destinationId in topLevelDestinations -> View.GONE
            destinationId in fullscreenDestinations -> View.GONE
            destinationId == R.id.orderSuccessFragment -> View.GONE
            else -> View.VISIBLE
        }
        binding.threeDotButton.visibility =
            if (destinationId == R.id.dashboard) View.VISIBLE else View.GONE
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
}
