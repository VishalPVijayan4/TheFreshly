package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentProductBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.AllProductsAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.CartViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ProductVM
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.utils.SearchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val productVM: ProductVM by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var adapter: AllProductsAdapter
    private var currentSearchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Products", "Shop from wide range of products")

        setupAdapter()
        setupRecyclerView()
        setupSearch()
        observeProducts()
        observeCart()
        observeCartCount()

        binding.btnPay.setOnClickListener {
            findNavController().navigate(R.id.action_product_to_payment)
        }

        return binding.root
    }

    private fun setupAdapter() {
        adapter = AllProductsAdapter(
            onClick = { product ->
                val bundle = Bundle().apply {
                    putString("categoryName", product.title)
                    product.id?.let { putInt("id", it) }
                }
                findNavController().navigate(R.id.action_product_to_productDetails3, bundle)
            },
            onAddToCart = { product -> cartViewModel.addToCart(product) },
            onIncrement = { product -> product.id?.let { cartViewModel.incrementQuantity(it) } },
            onDecrement = { product -> product.id?.let { cartViewModel.decrementQuantity(it) } },
            getCartQuantity = { productId -> cartViewModel.getQuantity(productId) }
        )
    }

    private fun setupRecyclerView() {
        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvProduct.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.progressbar.isVisible = loadState.refresh is LoadState.Loading

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
            errorState?.let {
                Toast.makeText(requireContext(), "Error: ${it.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSearch() {
        // Assuming you have an EditText with id etSearch in your layout
        binding.edtSearch?.let { searchEditText ->
            SearchHelper(
                searchEditText = searchEditText,
                lifecycleScope = lifecycleScope,
                debounceTime = 300L
            ) { query ->
                currentSearchQuery = query
                applySearch(query)
            }
        }
    }

    private fun applySearch(query: String) {
        lifecycleScope.launch {
            productVM.products.collectLatest { pagingData ->
                if (query.isEmpty()) {
                    adapter.submitData(pagingData)
                } else {
                    val filtered = pagingData.filter { product ->
                        product.title?.contains(query, ignoreCase = true) == true ||
                                product.brand?.contains(query, ignoreCase = true) == true ||
                                product.category?.contains(query, ignoreCase = true) == true
                    }
                    adapter.submitData(filtered)
                }
            }
        }
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            productVM.products.collectLatest { pagingData ->
                if (currentSearchQuery.isEmpty()) {
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeCart() {
        lifecycleScope.launch {
            cartViewModel.cartQuantities.collectLatest {
                adapter.notifyDataSetChanged() // Trigger UI update when cart changes
            }
        }
    }

    private fun observeCartCount() {
        lifecycleScope.launch {
            cartViewModel.totalCartCount.collectLatest { count ->
                // Update cart badge if you have one
                binding.tvCartBadge?.text = if (count > 0) count.toString() else ""
                binding.tvCartBadge?.isVisible = count > 0
            }
        }
    }
}
