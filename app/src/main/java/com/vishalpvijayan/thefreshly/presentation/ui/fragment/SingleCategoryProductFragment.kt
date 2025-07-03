package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentDashboardBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentProductBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentSingleCategoryProductBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.AllProductsAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.ProductCategoryAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.SingleCategoryProductsAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.DashboardViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ProductVM
import com.vishalpvijayan.thefreshly.presentation.vm.SingleCategoryProductVM
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue


class SingleCategoryProductFragment : Fragment() {

    private lateinit var binding : FragmentSingleCategoryProductBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val singleCategoryProductVM: SingleCategoryProductVM by activityViewModels()

    private lateinit var adapter: SingleCategoryProductsAdapter

//    private val args: SingleCategoryProductFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleCategoryProductBinding.inflate(inflater, container, false)

        val userId = arguments?.getString("categoryName")

        Toast.makeText(requireContext(), "Clicked: ${userId}", Toast.LENGTH_LONG).show()

//        val userId = args.userId
//        val userId = "smartphones"

        toolbarViewModel.setToolbarTitle(userId+"Products","Shop from wide range of products")

        adapter = SingleCategoryProductsAdapter { category ->

            val categoryName = category.title
            val id = category.id
            val bundle = Bundle().apply {
                putString("categoryName", categoryName)
                id?.let { putInt("id", it) }
            }

            findNavController().navigate(
                R.id.action_single_product_from_Category_to_productDetails,
                bundle
            )
            Toast.makeText(requireContext(), "Clicked: ${category.title}", Toast.LENGTH_SHORT).show()
        }


        lifecycleScope.launch {
            singleCategoryProductVM.getProductsByCategory(userId.toString()).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }




        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvProduct.adapter = adapter

        singleCategoryProductVM.setCategory(userId.toString())

        lifecycleScope.launch {
            singleCategoryProductVM.products.collectLatest {
                adapter.submitData(it)
            }
        }

        /*lifecycleScope.launch {
            singleCategoryProductVM.products.collectLatest {
                adapter.submitData(it)
            }
        }*/

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