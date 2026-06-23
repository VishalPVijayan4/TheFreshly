package com.vishalpvijayan.thefreshly.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.domain.repository.cart.CartRepository
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _product = MutableStateFlow<ProductDetail?>(null)
    val product: StateFlow<ProductDetail?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _cartItem = MutableStateFlow<CartItemEntity?>(null)
    val cartItem: StateFlow<CartItemEntity?> = _cartItem

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _product.value = null
                _cartItem.value = null

                val productDetail = getProductDetailUseCase(id)
                _product.value = productDetail
                _isLoading.value = false

                // Observe cart item for this product
                cartRepository.getCartItem(id).collectLatest { item ->
                    _cartItem.value = item
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("ProductDetailVM", "Failed to load product detail", e)
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            _product.value?.let { product ->
                val cartItem = CartItemEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    thumbnail = product.thumbnail,
                    quantity = 1,
                    stock = product.stock,
                    brand = product.brand,
                    category = product.category,
                    discountPercentage = product.discountPercentage
                )
                cartRepository.addToCart(cartItem)
            }
        }
    }

    fun incrementQuantity() {
        viewModelScope.launch {
            _product.value?.let { product ->
                if (_cartItem.value == null) {
                    addToCart()
                } else {
                    cartRepository.incrementQuantity(product.id)
                }
            }
        }
    }

    fun decrementQuantity() {
        viewModelScope.launch {
            _product.value?.let { product ->
                cartRepository.decrementQuantity(product.id)
            }
        }
    }
}
