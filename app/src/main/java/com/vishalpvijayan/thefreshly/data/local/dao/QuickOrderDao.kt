package com.vishalpvijayan.thefreshly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import com.vishalpvijayan.thefreshly.data.local.entity.QuickOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickOrderDao {
    @Query("SELECT * FROM quick_order_items ORDER BY orderedAt DESC")
    fun getQuickOrderItems(): Flow<List<QuickOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuickOrderItems(items: List<QuickOrderEntity>)

    @Query("DELETE FROM quick_order_items")
    suspend fun clearQuickOrderItems()

    @Transaction
    suspend fun replaceQuickOrderItems(items: List<CartItemEntity>) {
        clearQuickOrderItems()
        insertQuickOrderItems(items.map { it.toQuickOrderEntity() })
    }
}

private fun CartItemEntity.toQuickOrderEntity(): QuickOrderEntity = QuickOrderEntity(
    productId = productId,
    title = title,
    price = price,
    thumbnail = thumbnail,
    quantity = quantity,
    stock = stock,
    brand = brand,
    category = category,
    discountPercentage = discountPercentage,
    orderedAt = System.currentTimeMillis()
)
