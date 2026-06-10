package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
        toolbarViewModel.setToolbarTitle("Product details", "")

        val productId = arguments?.getInt("id") ?: 1
        viewModel.loadProductDetail(productId)

        setupClickListeners()
        observeProduct()
        observeCartItem()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.btnAdd.setOnClickListener {
            viewModel.addToCart()
        }

        binding.btnPlus.setOnClickListener {
            viewModel.incrementQuantity()
        }

        binding.btnMinus.setOnClickListener {
            viewModel.decrementQuantity()
        }
    }

    private fun observeProduct() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.collect { product ->
                    product?.let { bindProduct(it) }
                }
            }
        }
    }

    private fun observeCartItem() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItem.collect { cartItem ->
                    updateCartUI(cartItem?.quantity)
                }
            }
        }
    }

    private fun updateCartUI(quantity: Int?) {
        val isInCart = quantity != null && quantity > 0
        binding.llBtns.isVisible = isInCart
        binding.btnAdd.isVisible = !isInCart
        binding.tvQuantity.text = quantity?.toString().orEmpty()
    }

    private fun bindProduct(product: ProductDetail) {
        toolbarViewModel.setToolbarTitle(product.title, product.category)
        binding.tvDetailsBrand.text = product.title
        binding.tvTitle.text = product.title
        binding.tvBrandSku.text = "${product.brand} • SKU ${product.sku}"
        binding.tvCategory.text = "Category • ${product.category}"
        binding.tvTags.text = "Tags • ${product.tags.joinToString(" • ")}"
        binding.tvPrice.text = "$${String.format("%.2f", product.price)}"
        binding.tvRating.text = "${product.rating}★"
        binding.tvStockAvailability.text = "${product.stock} in stock • ${product.availabilityStatus}"
        binding.tvMOQ.text = "Minimum\n${product.minimumOrderQuantity}"
        binding.tvDescription.text = product.description
        binding.tvDimensions.text = "Dimensions • ${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.depth} cm\nWeight • ${product.weight}g"
        binding.tvWarranty.text = "Warranty • ${product.warrantyInformation}"
        binding.tvShipping.text = product.shippingInformation
        binding.tvReturnPolicy.text = "Returns • ${product.returnPolicy}"
        binding.tvMetadata.text = listOf(
            "ID: ${product.id}",
            "Discount: ${product.discountPercentage}%",
            "Created: ${product.meta.createdAt}",
            "Updated: ${product.meta.updatedAt}",
            "Barcode: ${product.meta.barcode}",
            "QR: ${product.meta.qrCode}",
            "Images: ${product.images.joinToString(", ")}"
        ).joinToString("\n")

        val reviews = product.reviews.take(3)
        binding.tvReviewHeader.text = "Reviews (${product.reviews.size})"
        binding.tvReview1.text = reviews.getOrNull(0)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: "No review yet"
        binding.tvReview2.text = reviews.getOrNull(1)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: ""
        binding.tvReview3.text = reviews.getOrNull(2)?.let { "${it.reviewerName} (${it.rating}★): ${it.comment}" } ?: ""

        binding.imgThumbnail.load(product.thumbnail) {
            crossfade(true)
            placeholder(R.drawable.image_icon)
            error(R.drawable.image_icon)
        }
    }
}
