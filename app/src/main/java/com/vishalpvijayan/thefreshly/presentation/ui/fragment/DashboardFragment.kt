package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.DialogCategoryListBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentDashboardBinding
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.domain.repository.location.LocationRepository
import com.vishalpvijayan.thefreshly.helper.LocationViewModel
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.CategoryListAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.CuratedProductAdapter
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.ProductCategoryAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.DashboardViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.UserDetailViewModel
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    @Inject
    lateinit var repo: LocationRepository
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val userDetailsVM: UserDetailViewModel by activityViewModels()
    private val dashBoardVm: DashboardViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by viewModels()


    private lateinit var adapter: ProductCategoryAdapter
    private lateinit var curatedProductAdapter: CuratedProductAdapter
    private var categoryDialog: androidx.appcompat.app.AlertDialog? = null

    override fun onDestroyView() {
        categoryDialog?.dismiss()
        categoryDialog = null
        binding.rvCategory.adapter = null
        binding.rvCuratedProducts.adapter = null
        locationViewModel.stopUpdates()
        _binding = null
        super.onDestroyView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Dashboard", "Manage & Explore various categories")
        adapter = ProductCategoryAdapter { category ->
            navigateToCategory(category)
        }

        curatedProductAdapter = CuratedProductAdapter { product ->
            val bundle = Bundle().apply {
                product.id?.let { putInt("id", it) }
            }
            findNavController().navigateSafely(R.id.productDetails, bundle)
        }

        locationViewModel.startUpdates()

        /*lifecycleScope.launchWhenStarted {
            locationViewModel.locationFlow.collect { location ->
                location?.let {
                    val lat = it.latitude
                    val lng = it.longitude
                    binding.txtAddress.text = "Lat: $lat, Lng: $lng"
                }
            }
        }*/

        binding.txtAddress.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigateSafely(R.id.action_dashboard_to_mapFragment)
        }



// Collect address updates
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationViewModel.addressFlow.collect { address ->
                    address?.let {
                        binding.txtAddress.text = it
                        Log.d("DashboardFragment", "Address: $it")
                    } ?: run {
                        binding.txtAddress.text = "Getting address..."
                    }
                }
            }
        }






        dashBoardVm.loadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Loading -> {
                    binding.viewFlipper.displayedChild = ConstantStrings.STATE_LOADING
                    binding.layoutLoading.visibility = View.VISIBLE
                    binding.layoutSuccess.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.viewFlipper.displayedChild = ConstantStrings.STATE_SUCCESS
                    binding.layoutLoading.visibility = View.GONE
                    binding.layoutSuccess.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                }
                is LoadState.Error -> {
                    binding.viewFlipper.displayedChild = ConstantStrings.STATE_ERROR
                    binding.layoutLoading.visibility = View.GONE
                    binding.layoutSuccess.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }

     /*   viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashBoardVm.loadState.asFlow().collectLatest { state ->
                    when (state) {
                        is LoadState.Loading -> {
                            binding.layoutLoading.visibility = View.VISIBLE
                            binding.layoutSuccess.visibility = View.GONE
                            binding.layoutError.visibility = View.GONE
                        }
                        is LoadState.NotLoading -> {
                            binding.layoutLoading.visibility = View.GONE
                            binding.layoutSuccess.visibility = View.VISIBLE
                            binding.layoutError.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            binding.layoutLoading.visibility = View.GONE
                            binding.layoutSuccess.visibility = View.GONE
                            binding.layoutError.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }*/


        /*adapter.addLoadStateListener { loadState ->
            dashBoardVm.setLoadState(loadState.refresh)

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
        }*/

        adapter.addLoadStateListener { loadStates ->
            dashBoardVm.setLoadState(loadStates.refresh)
        }

        /*adapter.addLoadStateListener { loadState ->
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
        }*/
        binding.rvCategory.apply {
            setHasFixedSize(true)
            itemAnimator = null
            overScrollMode = View.OVER_SCROLL_NEVER
            setItemViewCacheSize(8)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@DashboardFragment.adapter
            recycledViewPool.setMaxRecycledViews(0, 12)
        }

        binding.rvCuratedProducts.apply {
            setHasFixedSize(true)
            itemAnimator = null
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = curatedProductAdapter
        }
        binding.tvHeroSubtitle.text = "Your ${getTodayName()} morning\nessentials"
        dashBoardVm.loadAllCategories()
        dashBoardVm.loadCuratedProducts()

        binding.profilePic.setOnClickListener {
            findNavController().navigateSafely(R.id.action_dashboard_to_profile)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashBoardVm.categories.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashBoardVm.curatedProducts.collectLatest { products ->
                    curatedProductAdapter.submitList(products.take(4))
                }
            }
        }
        binding.tvWelcome.text = "Freshly"
        binding.ivNotification.setOnClickListener {
            findNavController().navigateSafely(R.id.action_dashboard_to_notificationFragment)
        }
        binding.searchContainer.setOnClickListener {
            findNavController().navigateSafely(R.id.action_dashboard_to_product)
        }
        binding.tvSeeAll.setOnClickListener {
            val categories = dashBoardVm.allCategories.value.ifEmpty { adapter.snapshot().items }
            showAllCategoriesDialog(categories)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsVM.userId.collect { userId ->

                    userDetailsVM.loadUserDetail(1)

                    userDetailsVM.user.collect { user ->
                        Log.d("UserData", "Received user: $user")
                        binding.profilePic.load(user?.image) {
                            crossfade(true)
                            placeholder(R.drawable.image_icon)
                            error(R.drawable.image_icon)
                        }
//                        binding.txtAddress.text = user?.address?.address.orEmpty() + ""+ user?.address?.city.orEmpty()+ ""+ user?.address?.state.orEmpty()+ ""+ user?.address?.postalCode.orEmpty()
                    }
                }
            }
        }
        return binding.root
    }

    private fun showAllCategoriesDialog(categories: List<ProductCategory>) {
        if (categories.isEmpty()) {
            Toast.makeText(requireContext(), "Categories are still loading", Toast.LENGTH_SHORT).show()
            dashBoardVm.loadAllCategories()
            return
        }

        val dialogBinding = DialogCategoryListBinding.inflate(layoutInflater)
        val dialogAdapter = CategoryListAdapter { category ->
            categoryDialog?.dismiss()
            categoryDialog = null
            navigateToCategory(category)
        }

        dialogBinding.rvDialogCategories.apply {
            setHasFixedSize(true)
            itemAnimator = null
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = dialogAdapter
        }
        dialogAdapter.submitList(categories)

        categoryDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnClose.setOnClickListener {
            categoryDialog?.dismiss()
            categoryDialog = null
        }
        categoryDialog?.setOnDismissListener { categoryDialog = null }
        categoryDialog?.show()
    }

    private fun navigateToCategory(category: ProductCategory) {
        val categoryName = category.name
        val bundle = Bundle().apply {
            putString("categoryName", categoryName)
        }
        findNavController().navigateSafely(
            R.id.action_dashboard_to_single_product_from_Category,
            bundle
        )
        Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
    }

    private fun getTodayName(): String {
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    }
}
