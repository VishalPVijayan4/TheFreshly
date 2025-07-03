package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.databinding.FragmentProductDetailsBinding
import com.vishalpvijayan.thefreshly.presentation.vm.ProductDetailViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Product Details", "Details of the selected product")

        val productId = arguments?.getInt("id") ?: 1
        viewModel.loadProductDetail(productId)

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.collect { product ->
                    product?.let { bindProduct(it) }
                }
            }
        }

        return binding.root
    }

    private fun bindProduct(product: ProductDetail) {
        binding.tvTitle.text = product.title
        binding.tvBrandSku.text = "Brand: ${product.brand} | SKU: ${product.sku}"
        binding.tvCategory.text = "Category: ${product.category}"
        binding.tvTags.text = "Tags: ${product.tags.joinToString(", ")}"
        binding.tvPrice.text = "Price: \$${product.price} (-${product.discountPercentage}%)"
        binding.tvRating.text = "Rating: ${product.rating} / 5"
        binding.tvStockAvailability.text = "Stock: ${product.stock} | Availability: ${product.availabilityStatus}"
        binding.tvMOQ.text = "Minimum Order Quantity: ${product.minimumOrderQuantity}"
        binding.tvDescription.text = product.description
        binding.tvDimensions.text = "Dimensions: ${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.depth} cm | Weight: ${product.weight}g"
        binding.tvWarranty.text = "Warranty: ${product.warrantyInformation}"
        binding.tvShipping.text = product.shippingInformation
        binding.tvReturnPolicy.text = product.returnPolicy
        binding.tvMetadata.text = "Created At: ${product.meta.createdAt} | Barcode: ${product.meta.barcode}"

        // Reviews
        val reviews = product.reviews.take(3)
        binding.tvReviewHeader.text = "Reviews"
        binding.tvReview1.text = reviews.getOrNull(0)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: ""
        binding.tvReview2.text = reviews.getOrNull(1)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: ""
        binding.tvReview3.text = reviews.getOrNull(2)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: ""

        binding.btnAdd.text = "Add to Cart"
        binding.tvQuantity.text = "1"

        // Load thumbnail using Coil
        binding.imgThumbnail.load(product.thumbnail) {
            crossfade(true)
            placeholder(R.drawable.image_icon)
            error(R.drawable.image_icon)
        }
    }
}

