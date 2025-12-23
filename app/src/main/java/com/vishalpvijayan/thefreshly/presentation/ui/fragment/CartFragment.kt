package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("My Cart", "Review your items")

        setupAdapter()
        setupRecyclerView()
        observeCart()
        observeTotalPrice()

        binding.btnCheckout.setOnClickListener {
            // Navigate to checkout/payment
            findNavController().navigate(R.id.action_cartFragment_to_mapFragment)
        }

        binding.btnClearCart.setOnClickListener {
            cartViewModel.clearCart()
        }

        return binding.root
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
                findNavController().navigate(R.id.action_cartFragment_to_productDetails, bundle)
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
                binding.btnCheckout.isEnabled = items.isNotEmpty()
            }
        }
    }

    private fun observeTotalPrice() {
        lifecycleScope.launch {
            cartViewModel.totalCartPrice.collectLatest { total ->
                binding.tvTotalPrice.text = "Total: $${"%.2f".format(total)}"
            }
        }

        lifecycleScope.launch {
            cartViewModel.totalCartCount.collectLatest { count ->
                binding.tvItemCount.text = "$count items"
            }
        }
    }
}
