package com.vishalpvijayan.thefreshly

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.razorpay.PaymentData
import com.vishalpvijayan.thefreshly.databinding.ActivityMainBinding
import com.vishalpvijayan.thefreshly.presentation.vm.CartViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.LoginVM
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),com.razorpay.PaymentResultWithDataListener,
    com.razorpay.ExternalWalletListener {

    private lateinit var binding: ActivityMainBinding
    private val toolbarViewModel: ToolbarViewModel by viewModels()
    private val loginViewModel: LoginVM by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    private var currentDestinationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        binding.bottomNavView.setOnItemSelectedListener { item ->
            if (currentDestinationId == item.itemId) {
                true
            } else {
                runCatching {
                    navController.navigate(item.itemId)
                }.isSuccess
            }
        }

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Observe cart count for FAB visibility
        observeCartCount()

        // Setup cart FAB click listener
        val openCart = View.OnClickListener {
            navController.navigate(R.id.cartFragment)
        }
        binding.cart.setOnClickListener(openCart)
        binding.fabContainer.setOnClickListener(openCart)

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

        // Observe Toolbar Subtitle
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                toolbarViewModel.toolbarSubTitle.collect { subTitle ->
                    binding.customToolbar.findViewById<TextView>(R.id.toolbarSubTitle).text = subTitle
                }
            }
        }



        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            updateFabVisibility()

            when (destination.id) {
                R.id.SettingsFragment -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.GONE
                }
                R.id.SupportChatFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                }
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
                R.id.cartFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                }
                R.id.product -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.customToolbar.visibility = View.GONE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.GONE
                }
                R.id.single_product_from_Category -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                    binding.ivBack.visibility = View.VISIBLE
                }
                R.id.dashboard -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.customToolbar.visibility = View.GONE
                    binding.threeDotButton.visibility = View.VISIBLE
                    binding.ivBack.visibility = View.GONE
                }
                R.id.productDetails -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.ivBack.visibility = View.VISIBLE
                    binding.threeDotButton.visibility = View.GONE
                }
                R.id.orderSuccessFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    binding.customToolbar.visibility = View.VISIBLE
                    binding.ivBack.visibility = View.GONE
                    binding.threeDotButton.visibility = View.GONE
                }
                R.id.onboarding, R.id.splash, R.id.login,
                R.id.forgotpassword, R.id.createccount,
                R.id.mapFragment, R.id.payment -> {
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

    private fun observeCartCount() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.totalCartCount.collect { count ->
                    Log.d("MainActivity", "Cart count updated: $count")

                    // Access the parent FrameLayout
                    val fabParent = binding.cart.parent as? View

                    // Screens where FAB should be hidden
                    val hideOnScreens = setOf(
                        R.id.onboarding,
                        R.id.splash,
                        R.id.login,
                        R.id.forgotpassword,
                        R.id.createccount,
                        R.id.mapFragment,
                        R.id.payment,
                        R.id.cartFragment,
                        R.id.SettingsFragment,
                        R.id.SupportChatFragment
                    )

                    val shouldShow = count > 0 && currentDestinationId !in hideOnScreens

                    fabParent?.isVisible = shouldShow

                    Log.d("MainActivity", "FAB should show: $shouldShow (count: $count, screen: $currentDestinationId)")
                }
            }
        }
    }





    private fun updateFabVisibility() {
        val cartCount = cartViewModel.totalCartCount.value

        // Screens where FAB should NEVER show regardless of cart count
        val hideOnScreens = setOf(
            R.id.onboarding,
            R.id.splash,
            R.id.login,
            R.id.forgotpassword,
            R.id.createccount,
            R.id.mapFragment,
            R.id.payment,
            R.id.cartFragment,
            R.id.SupportChatFragment,
            R.id.orderSuccessFragment// Don't show on cart screen itself
        )

        val shouldShow = cartCount > 0 && currentDestinationId !in hideOnScreens

        Log.d("MainActivity", "FAB visibility - count: $cartCount, destination: $currentDestinationId, shouldShow: $shouldShow")

        binding.fabContainer.isVisible = shouldShow
    }

    fun startRazorpayPayment(options: org.json.JSONObject) {
        val checkout = com.razorpay.Checkout()
        checkout.setKeyID("rzp_test_RvYknb590BqzaM")
        checkout.open(this, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        com.razorpay.Checkout.handleActivityResult(this, requestCode, resultCode, data, this, this)
    }



    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Log.d("MainActivity", "PAYMENT SUCCESS:")
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.e("MainActivity", "PAYMENT ERROR: code= response=")
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }

}
