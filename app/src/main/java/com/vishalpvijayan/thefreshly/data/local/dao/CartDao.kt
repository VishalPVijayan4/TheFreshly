package com.vishalpvijayan.thefreshly.data.local.dao

import androidx.room.*
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItem(productId: Int): CartItemEntity?

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    fun getCartItemFlow(productId: Int): Flow<CartItemEntity?>

    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    suspend fun getAllCartItemsOnce(): List<CartItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItemById(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun getTotalCartCount(): Flow<Int?>

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun getTotalCartPrice(): Flow<Double?>
}
