package com.vishalpvijayan.thefreshly.presentation.ui.fragment


import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentSettingBinding
import com.vishalpvijayan.thefreshly.presentation.vm.SettingsViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private var logoutSwipeAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.setToolbarTitle("Settings", "Manage your account")

        setupClickListeners()
        setupSwipeToLogout()
    }

    private fun setupClickListeners() {
        binding.cardAbout.setOnClickListener {
            showAboutDialog()
        }

        binding.cardPrivacy.setOnClickListener {
            showPrivacyPolicyDialog()
        }

        binding.cardSupport.setOnClickListener {
            findNavController().navigateSafely(R.id.action_SettingsFragment_to_SupportChatFragment)
        }

    }

    private fun setupSwipeToLogout() {
        var dragStartX = 0f
        var thumbStartTranslation = 0f

        binding.swipeLogout.setOnTouchListener { _, event ->
            if (!binding.swipeLogout.isEnabled) return@setOnTouchListener false
            val maxTranslation = (binding.swipeLogout.width - binding.logoutSwipeThumb.width -
                binding.swipeLogout.paddingStart - binding.swipeLogout.paddingEnd).toFloat()

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    logoutSwipeAnimator?.cancel()
                    dragStartX = event.x
                    thumbStartTranslation = binding.logoutSwipeThumb.translationX
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val translation = (thumbStartTranslation + event.x - dragStartX)
                        .coerceIn(0f, maxTranslation)
                    binding.logoutSwipeThumb.translationX = translation
                    binding.tvLogoutSwipeLabel.alpha = 1f - (translation / maxTranslation) * 0.65f
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (binding.logoutSwipeThumb.translationX >= maxTranslation * SWIPE_COMPLETE_THRESHOLD) {
                        binding.logoutSwipeThumb.animate()
                            .translationX(maxTranslation)
                            .setDuration(120L)
                            .withEndAction { showLogoutConfirmation() }
                            .start()
                    } else {
                        resetLogoutSwipeThumb()
                    }
                    binding.swipeLogout.performClick()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    resetLogoutSwipeThumb()
                    true
                }
                else -> false
            }
        }
        binding.swipeLogout.setOnClickListener { }
        binding.swipeLogout.post { startLogoutSwipeHintAnimation() }
    }

    private fun resetLogoutSwipeThumb() {
        binding.logoutSwipeThumb.animate()
            .translationX(0f)
            .setDuration(220L)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction { startLogoutSwipeHintAnimation() }
            .start()
        binding.tvLogoutSwipeLabel.animate().alpha(1f).setDuration(180L).start()
    }

    private fun startLogoutSwipeHintAnimation() {
        if (!isAdded || !binding.swipeLogout.isShown) return
        logoutSwipeAnimator?.cancel()
        logoutSwipeAnimator = ObjectAnimator.ofFloat(binding.logoutSwipeThumb, View.TRANSLATION_X, 0f, 30f, 0f).apply {
            duration = 1400L
            startDelay = 500L
            repeatCount = ObjectAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    private fun showAboutDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_scrollable_content, null)

        val titleView = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val contentView = dialogView.findViewById<TextView>(R.id.dialogContent)

        titleView.text = "About Freshly"
        contentView.text = """
            Freshly - Your Fresh Grocery Delivery Partner
            
            Version: 1.0.0
            
            Welcome to Freshly, your one-stop solution for fresh groceries delivered to your doorstep within minutes!
            
            Our Mission:
            To provide the freshest groceries to our customers with lightning-fast delivery and exceptional service quality.
            
            What We Offer:
            • Fresh fruits and vegetables
            • Dairy products
            • Bakery items
            • Pantry essentials
            • Beverages
            • Personal care products
            
            Why Choose Freshly?
            ✓ 10-minute delivery guarantee
            ✓ Quality-checked products
            ✓ Competitive pricing
            ✓ Easy returns and refunds
            ✓ 24/7 customer support
            
            Our Promise:
            We are committed to delivering only the freshest products. If you're not satisfied with the quality, we offer hassle-free returns and full refunds.
            
            Contact Information:
            Email: support@freshly.com
            Phone: +91 6361777579
            Website: www.freshly.com
            
            Follow Us:
            Instagram: @freshly_official
            Facebook: /FreshlyApp
            Twitter: @FreshlyApp
            
            Thank you for choosing Freshly!
            Making fresh groceries accessible to everyone, everywhere.
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .create()
            .show()
    }

    private fun showPrivacyPolicyDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_scrollable_content, null)

        val titleView = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val contentView = dialogView.findViewById<TextView>(R.id.dialogContent)

        titleView.text = "Privacy Policy"
        contentView.text = """
            Freshly Privacy Policy
            Last Updated: December 2025
            
            1. Introduction
            Welcome to Freshly. We respect your privacy and are committed to protecting your personal data. This privacy policy will inform you about how we look after your personal data when you use our app and tell you about your privacy rights.
            
            2. Information We Collect
            We collect and process the following data about you:
            
            • Personal Information:
              - Name, email address, phone number
              - Delivery address
              - Payment information
            
            • Usage Data:
              - App usage statistics
              - Device information
              - Location data (for delivery)
            
            • Transaction Data:
              - Purchase history
              - Payment details
              - Delivery preferences
            
            3. How We Use Your Information
            We use your data to:
            • Process and deliver your orders
            • Manage your account
            • Send order updates and notifications
            • Improve our services
            • Provide customer support
            • Detect and prevent fraud
            
            4. Data Security
            We implement appropriate security measures to protect your personal data against unauthorized access, alteration, disclosure, or destruction.
            
            • Encrypted data transmission
            • Secure payment processing
            • Regular security audits
            • Access controls and authentication
            
            5. Data Sharing
            We do not sell your personal data. We may share your data with:
            • Delivery partners (for order fulfillment)
            • Payment processors (for transactions)
            • Service providers (for app functionality)
            
            All third parties are required to maintain data security and confidentiality.
            
            6. Your Rights
            You have the right to:
            • Access your personal data
            • Correct inaccurate data
            • Request data deletion
            • Object to data processing
            • Withdraw consent
            • Data portability
            
            7. Cookies and Tracking
            We use cookies and similar technologies to:
            • Remember your preferences
            • Analyze app usage
            • Improve user experience
            
            8. Children's Privacy
            Our services are not intended for users under 18 years of age. We do not knowingly collect data from children.
            
            9. Changes to This Policy
            We may update this privacy policy from time to time. We will notify you of any changes by posting the new policy on this page.
            
            10. Contact Us
            If you have questions about this privacy policy, please contact us at:
            Email: privacy@freshly.com
            Phone: +91 6361777579
            
            Your privacy matters to us!
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .create()
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun performLogout() {
        binding.swipeLogout.isEnabled = false
        settingsViewModel.logout {
            if (_binding != null && findNavController().currentDestination?.id == R.id.SettingsFragment) {
                findNavController().navigateSafely(R.id.action_SettingsFragment_to_login)
            }
        }
    }

    override fun onDestroyView() {
        logoutSwipeAnimator?.cancel()
        logoutSwipeAnimator = null
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val SWIPE_COMPLETE_THRESHOLD = 0.75f
    }
}
