package com.vishalpvijayan.thefreshly.presentation.vm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import com.vishalpvijayan.thefreshly.data.local.entity.QuickOrderEntity
import com.vishalpvijayan.thefreshly.domain.repository.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // All cart items for cart screen
    val cartItems: StateFlow<List<CartItemEntity>> = cartRepository.getAllCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Map of productId -> quantity for quick lookup in adapters
    val cartQuantities: StateFlow<Map<Int, Int>> = cartRepository.getAllCartItems()
        .map { items ->
            items.associate { it.productId to it.quantity }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    // Total count badge
    val totalCartCount: StateFlow<Int> = cartRepository.getTotalCartCount()
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val quickOrderItems: StateFlow<List<QuickOrderEntity>> = cartRepository.getQuickOrderItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Total price
    val totalCartPrice: StateFlow<Double> = cartRepository.getTotalCartPrice()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun addToCart(product: Products) {
        viewModelScope.launch {
            val cartItem = CartItemEntity(
                productId = product.id ?: return@launch,
                title = product.title ?: "",
                price = product.price ?: 0.0,
                thumbnail = product.thumbnail ?: "",
                quantity = 1,
                stock = product.stock ?: 0,
                brand = product.brand ?: "",
                category = product.category ?: "",
                discountPercentage = product.discountPercentage ?: 0.0
            )
            cartRepository.addToCart(cartItem)
        }
    }

    fun incrementQuantity(productId: Int) {
        viewModelScope.launch {
            cartRepository.incrementQuantity(productId)
        }
    }

    fun decrementQuantity(productId: Int) {
        viewModelScope.launch {
            cartRepository.decrementQuantity(productId)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun getQuantity(productId: Int): Int? {
        return cartQuantities.value[productId]
    }

    fun completeSuccessfulPayment(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            cartRepository.completeSuccessfulPayment()
            onComplete()
        }
    }

    fun addQuickOrderToCart() {
        viewModelScope.launch {
            cartRepository.addQuickOrderToCart(quickOrderItems.value)
        }
    }
}
