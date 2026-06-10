package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentOnBoardingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.vishalpvijayan.thefreshly.utils.navigateSafely

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupAnimations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        binding.btnGetStarted.setOnClickListener {
            val action = OnBoardingFragmentDirections.actionOnboardingToLogin()
            findNavController().navigateSafely(action)
        }
    }

    private fun setupAnimations() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            animateCardsSequentially()
            createDotIndicator()
        }
    }

    private fun animateCardsSequentially() {
        val cards = listOf(
            binding.card1,
            binding.card2,
            binding.card3
        )

        cards.forEachIndexed { index, card ->
            val animatorSet = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(card, "alpha", 0f, 1f),
                    ObjectAnimator.ofFloat(card, "scaleX", 0.8f, 1f),
                    ObjectAnimator.ofFloat(card, "scaleY", 0.8f, 1f),
                    ObjectAnimator.ofFloat(card, "translationY", 50f, 0f)
                )
                duration = 800
                startDelay = (index * 400L)
                interpolator = OvershootInterpolator(1.2f)
            }
            animatorSet.start()
        }
    }

    private fun createDotIndicator() {
        val dotIndicator = binding.dotIndicator

        repeat(3) { index ->
            val dot = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(12.dp, 12.dp).apply {
                    setMargins(6.dp, 0, 6.dp, 0)
                }
                background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_apple)
                alpha = if (index == 0) 1f else 0.3f

                // Animate dots sequentially
                animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setStartDelay((index * 200L))
                    .start()
            }
            dotIndicator.addView(dot)
        }
    }
}

// Extension function for dp conversion
private val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
