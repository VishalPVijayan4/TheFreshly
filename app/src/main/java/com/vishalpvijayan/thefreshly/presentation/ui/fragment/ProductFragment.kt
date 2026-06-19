package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val productVM: ProductVM by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var adapter: AllProductsAdapter
    private var searchHelper: SearchHelper? = null
    private val searchQuery = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarViewModel.setToolbarTitle("Products", "Shop from wide range of products")

        setupAdapter()
        setupRecyclerView()
        setupSearch()
        observeProducts()
        observeCart()
        observeCartCount()

        binding.tvCartBadge.setOnClickListener {
            findNavController().navigateSafely(R.id.action_product_to_cartFragment)
        }
    }

    private fun setupAdapter() {
        adapter = AllProductsAdapter(
            onClick = { product ->
                val bundle = Bundle().apply {
                    putString("categoryName", product.title)
                    product.id?.let { putInt("id", it) }
                }
                findNavController().navigateSafely(R.id.action_product_to_productDetails3, bundle)
            },
            onAddToCart = { product -> cartViewModel.addToCart(product) },
            onIncrement = { product -> product.id?.let { cartViewModel.incrementQuantity(it) } },
            onDecrement = { product -> product.id?.let { cartViewModel.decrementQuantity(it) } },
            getCartQuantity = { productId -> cartViewModel.getQuantity(productId) }
        )
    }

    private fun setupRecyclerView() {
        binding.rvProduct.setHasFixedSize(true)
        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            val currentBinding = _binding ?: return@addLoadStateListener
            currentBinding.progressbar.isVisible = loadState.refresh is LoadState.Loading

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
            errorState?.let {
                Toast.makeText(requireContext(), "Error: ${it.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSearch() {
        searchHelper = SearchHelper(
            searchEditText = binding.etSearch,
            lifecycleScope = viewLifecycleOwner.lifecycleScope,
            debounceTime = 300L
        ) { query ->
            searchQuery.value = query
        }
    }

    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(productVM.products, searchQuery) { pagingData, query ->
                    if (query.isBlank()) {
                        pagingData
                    } else {
                        pagingData.filter { product ->
                            product.title?.contains(query, ignoreCase = true) == true ||
                                    product.brand?.contains(query, ignoreCase = true) == true ||
                                    product.category?.contains(query, ignoreCase = true) == true
                        }
                    }
                }.collectLatest { displayData ->
                    adapter.submitData(displayData)
                }
            }
        }
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                var previousQuantities = emptyMap<Int, Int>()
                cartViewModel.cartQuantities.collectLatest { quantities ->
                    adapter.updateCartQuantities(previousQuantities, quantities)
                    previousQuantities = quantities
                }
            }
        }
    }

    private fun observeCartCount() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.totalCartCount.collectLatest { count ->
                    binding.tvCartBadge.text = if (count > 0) count.toString() else ""
                    binding.tvCartBadge.isVisible = count > 0
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.rvProduct.adapter = null
        searchHelper?.dispose()
        searchHelper = null
        _binding = null
        super.onDestroyView()
    }
}
