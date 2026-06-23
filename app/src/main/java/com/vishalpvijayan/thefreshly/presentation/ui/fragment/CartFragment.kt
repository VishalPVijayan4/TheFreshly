package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentCartBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.CartAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.CartViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private lateinit var cartAdapter: CartAdapter
    private var swipeHintAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Freshly", "")

        setupAdapter()
        setupRecyclerView()
        observeCart()
        observeTotalPrice()

        setupSwipeToCheckout()

        binding.btnClearCart.setOnClickListener {
            cartViewModel.clearCart()
        }

        return binding.root
    }

    private fun setupSwipeToCheckout() {
        var dragStartX = 0f
        var thumbStartTranslation = 0f

        binding.swipeCheckout.setOnTouchListener { _, event ->
            if (!binding.swipeCheckout.isEnabled) return@setOnTouchListener false

            val maxTranslation = (binding.swipeCheckout.width - binding.swipeThumb.width -
                binding.swipeCheckout.paddingStart - binding.swipeCheckout.paddingEnd).toFloat()

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    swipeHintAnimator?.cancel()
                    dragStartX = event.x
                    thumbStartTranslation = binding.swipeThumb.translationX
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val translation = (thumbStartTranslation + event.x - dragStartX)
                        .coerceIn(0f, maxTranslation)
                    binding.swipeThumb.translationX = translation
                    binding.tvSwipeLabel.alpha = 1f - (translation / maxTranslation) * 0.65f
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (binding.swipeThumb.translationX >= maxTranslation * SWIPE_COMPLETE_THRESHOLD) {
                        binding.swipeThumb.animate()
                            .translationX(maxTranslation)
                            .setDuration(120L)
                            .withEndAction {
                                findNavController().navigateSafely(
                                    R.id.action_cartFragment_to_mapFragment
                                )
                            }
                            .start()
                    } else {
                        resetSwipeThumb()
                    }
                    binding.swipeCheckout.performClick()
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    resetSwipeThumb()
                    true
                }

                else -> false
            }
        }

        binding.swipeCheckout.setOnClickListener { }
        binding.swipeCheckout.post { startSwipeHintAnimation() }
    }

    private fun resetSwipeThumb() {
        binding.swipeThumb.animate()
            .translationX(0f)
            .setDuration(220L)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction { startSwipeHintAnimation() }
            .start()
        binding.tvSwipeLabel.animate().alpha(1f).setDuration(180L).start()
    }

    private fun startSwipeHintAnimation() {
        if (!isAdded || !binding.swipeCheckout.isShown) return
        swipeHintAnimator?.cancel()
        swipeHintAnimator = ObjectAnimator.ofFloat(binding.swipeThumb, View.TRANSLATION_X, 0f, 30f, 0f).apply {
            duration = 1400L
            startDelay = 500L
            repeatCount = ObjectAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    override fun onDestroyView() {
        swipeHintAnimator?.cancel()
        swipeHintAnimator = null
        super.onDestroyView()
    }

    companion object {
        private const val SWIPE_COMPLETE_THRESHOLD = 0.75f
    }

    private fun setupAdapter() {
        cartAdapter = CartAdapter(
            onIncrement = { item -> cartViewModel.incrementQuantity(item.productId) },
            onDecrement = { item -> cartViewModel.decrementQuantity(item.productId) },
            onRemove = { item -> cartViewModel.removeFromCart(item.productId) },
            onClick = { item ->
                val bundle = Bundle().apply {
                    putInt("id", item.productId)
                }
                findNavController().navigateSafely(R.id.action_cartFragment_to_productDetails, bundle)
            }
        )
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        lifecycleScope.launch {
            cartViewModel.cartItems.collectLatest { items ->
                cartAdapter.submitList(items)
                binding.emptyCartView.isVisible = items.isEmpty()
                binding.rvCart.isVisible = items.isNotEmpty()
                binding.swipeCheckout.isEnabled = items.isNotEmpty()
                binding.checkoutDock.isVisible = items.isNotEmpty()
            }
        }
    }

    private fun observeTotalPrice() {
        lifecycleScope.launch {
            cartViewModel.totalCartPrice.collectLatest { total ->
                val platformFee = if (total > 0.0) 0.50 else 0.0
                val estimatedTaxes = if (total > 0.0) total * 0.07 else 0.0
                val toPay = total + platformFee + estimatedTaxes

                binding.tvItemsSubtotal.text = "$${"%.2f".format(total)}"
                binding.tvTaxes.text = "$${"%.2f".format(estimatedTaxes)}"
                binding.tvTotalPrice.text = "$${"%.2f".format(toPay)}"
                binding.tvDockTotal.text = "$${"%.2f".format(toPay)} Total"
            }
        }

        lifecycleScope.launch {
            cartViewModel.totalCartCount.collectLatest { count ->
                binding.tvItemCount.text = "$count ITEMS"
            }
        }
    }
}
