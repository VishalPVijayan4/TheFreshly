package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentDashboardBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentProductBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.AllProductsAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.ProductCategoryAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.DashboardViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ProductVM
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue


class ProductFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val productVM: ProductVM by activityViewModels()

    private lateinit var adapter: AllProductsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Products","Shop from wide range of products")


        adapter = AllProductsAdapter { category ->
            Toast.makeText(requireContext(), "Clicked: ${category.title}", Toast.LENGTH_SHORT).show()
        }

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvProduct.adapter = adapter

        lifecycleScope.launch {
            productVM.products.collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.progressbar.visibility = if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
            errorState?.let {
                Toast.makeText(requireContext(), "Error: ${it.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

}