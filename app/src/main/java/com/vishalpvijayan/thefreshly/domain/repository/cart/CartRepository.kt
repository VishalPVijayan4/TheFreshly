package com.vishalpvijayan.thefreshly.domain.repository.cart


import com.vishalpvijayan.thefreshly.data.local.dao.CartDao
import com.vishalpvijayan.thefreshly.data.local.dao.QuickOrderDao
import com.vishalpvijayan.thefreshly.data.local.entity.QuickOrderEntity
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao,
    private val quickOrderDao: QuickOrderDao
) {

    fun getCartItem(productId: Int): Flow<CartItemEntity?> {
        return cartDao.getCartItemFlow(productId)
    }

    suspend fun getCartItemOnce(productId: Int): CartItemEntity? {
        return cartDao.getCartItem(productId)
    }

    fun getAllCartItems(): Flow<List<CartItemEntity>> {
        return cartDao.getAllCartItems()
    }

    suspend fun addToCart(cartItem: CartItemEntity) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItemEntity) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun incrementQuantity(productId: Int) {
        val item = cartDao.getCartItem(productId)
        item?.let {
            if (it.quantity < it.stock) {
                cartDao.updateCartItem(it.copy(quantity = it.quantity + 1))
            }
        }
    }

    suspend fun decrementQuantity(productId: Int) {
        val item = cartDao.getCartItem(productId)
        item?.let {
            if (it.quantity > 1) {
                cartDao.updateCartItem(it.copy(quantity = it.quantity - 1))
            } else {
                cartDao.deleteCartItem(it)
            }
        }
    }

    suspend fun removeFromCart(productId: Int) {
        cartDao.deleteCartItemById(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    fun getTotalCartCount(): Flow<Int?> {
        return cartDao.getTotalCartCount()
    }

    fun getTotalCartPrice(): Flow<Double?> {
        return cartDao.getTotalCartPrice()
    }

    fun getQuickOrderItems(): Flow<List<QuickOrderEntity>> {
        return quickOrderDao.getQuickOrderItems()
    }

    suspend fun completeSuccessfulPayment() {
        val currentCart = cartDao.getAllCartItemsOnce()
        if (currentCart.isNotEmpty()) {
            quickOrderDao.replaceQuickOrderItems(currentCart)
        }
        cartDao.clearCart()
    }

    suspend fun addQuickOrderToCart(items: List<QuickOrderEntity>) {
        items.forEach { quickOrderItem ->
            cartDao.insertCartItem(quickOrderItem.toCartItem())
        }
    }
}
