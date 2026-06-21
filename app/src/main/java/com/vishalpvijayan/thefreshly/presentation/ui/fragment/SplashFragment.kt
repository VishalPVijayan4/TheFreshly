package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.presentation.vm.LoginVM
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val loginViewModel: LoginVM by viewModels()
    private val splashAnimators = mutableListOf<ObjectAnimator>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSplashAnimations(view)

        viewLifecycleOwner.lifecycleScope.launch {
            val isLoggedIn = loginViewModel.isLoggedIn.first()
            delay(1400)
            if (findNavController().currentDestination?.id != R.id.splash) return@launch

            val action = if (isLoggedIn) {
                SplashFragmentDirections.actionSplashToDashboard()
            } else {
                SplashFragmentDirections.actionSplashToOnboarding()
            }
            findNavController().navigateSafely(action)
        }
    }

    override fun onDestroyView() {
        splashAnimators.forEach { it.cancel() }
        splashAnimators.clear()
        super.onDestroyView()
    }

    private fun startSplashAnimations(view: View) {
        val logo = view.findViewById<View>(R.id.ivLogo)
        val logoGlow = view.findViewById<View>(R.id.logoGlow)
        val appName = view.findViewById<View>(R.id.tvAppName)
        val quote = view.findViewById<View>(R.id.tvQuote)
        val progressFill = view.findViewById<View>(R.id.progressFill)
        val orbMint = view.findViewById<View>(R.id.orbMint)
        val orbBlue = view.findViewById<View>(R.id.orbBlue)
        val dots = listOf(
            view.findViewById<View>(R.id.dotOne),
            view.findViewById<View>(R.id.dotTwo),
            view.findViewById<View>(R.id.dotThree)
        )

        listOf(logo, logoGlow, appName, quote).forEach { it.alpha = 0f }
        logo.scaleX = 0.72f
        logo.scaleY = 0.72f
        logo.rotation = -8f
        logoGlow.scaleX = 0.58f
        logoGlow.scaleY = 0.58f
        progressFill.pivotX = 0f

        appName.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(520L)
            .setInterpolator(DecelerateInterpolator())
            .start()

        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .rotation(0f)
            .setStartDelay(160L)
            .setDuration(700L)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        logoGlow.animate()
            .alpha(0.86f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(120L)
            .setDuration(760L)
            .setInterpolator(DecelerateInterpolator())
            .start()

        quote.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(420L)
            .setDuration(520L)
            .setInterpolator(DecelerateInterpolator())
            .start()

        progressFill.animate()
            .scaleX(1f)
            .setStartDelay(360L)
            .setDuration(900L)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        splashAnimators += pulseAnimator(logoGlow, "scaleX", 0.96f, 1.08f, 1300L)
        splashAnimators += pulseAnimator(logoGlow, "scaleY", 0.96f, 1.08f, 1300L)
        splashAnimators += floatAnimator(orbMint, "translationY", -18f, 22f, 3600L)
        splashAnimators += floatAnimator(orbBlue, "translationX", -20f, 18f, 4200L)
        dots.forEachIndexed { index, dot ->
            splashAnimators += pulseAnimator(dot, "scaleX", 0.6f, 1.3f, 620L, index * 120L)
            splashAnimators += pulseAnimator(dot, "scaleY", 0.6f, 1.3f, 620L, index * 120L)
            splashAnimators += pulseAnimator(dot, "alpha", 0.35f, 1f, 620L, index * 120L)
        }
        splashAnimators.forEach { it.start() }
    }

    private fun pulseAnimator(
        target: View,
        property: String,
        from: Float,
        to: Float,
        duration: Long,
        delay: Long = 0L
    ): ObjectAnimator = ObjectAnimator.ofFloat(target, property, from, to).apply {
        this.duration = duration
        startDelay = delay
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
    }

    private fun floatAnimator(
        target: View,
        property: String,
        from: Float,
        to: Float,
        duration: Long
    ): ObjectAnimator = pulseAnimator(target, property, from, to, duration)

}
