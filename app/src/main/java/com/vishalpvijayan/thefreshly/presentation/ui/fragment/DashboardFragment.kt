package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentDashboardBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentLoginBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.ProductCategoryAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.StaticBannerAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.DashboardViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.utils.CategoryListItem
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val dashBoardVm: DashboardViewModel by activityViewModels()

    private lateinit var adapter: ProductCategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Dashboard", "Manage & Explore various categories")


        adapter = ProductCategoryAdapter { category ->


            val categoryName = category.name
            val bundle = Bundle().apply {
                putString("categoryName", categoryName)
            }

            findNavController().navigate(
                R.id.action_dashboard_to_single_product_from_Category,
                bundle
            )
            Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
        }



        adapter.addLoadStateListener { loadState ->
            binding.progressbar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "Error: ${it.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }



        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvCategory.adapter = adapter

        lifecycleScope.launch {
            dashBoardVm.categories.collectLatest {
                adapter.submitData(it)
            }
        }




        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashBoardVm.usernameFlow.collect { username ->
                    binding.tvWelcome.text = ConstantStrings.welcome + username
                }
            }
        }




        return binding.root
    }
}