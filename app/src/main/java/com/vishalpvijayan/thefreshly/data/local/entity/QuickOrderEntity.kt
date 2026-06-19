package com.vishalpvijayan.thefreshly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quick_order_items")
data class QuickOrderEntity(
    @PrimaryKey
    val productId: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val quantity: Int,
    val stock: Int,
    val brand: String,
    val category: String,
    val discountPercentage: Double = 0.0,
    val orderedAt: Long = System.currentTimeMillis()
) {
    fun toCartItem(): CartItemEntity = CartItemEntity(
        productId = productId,
        title = title,
        price = price,
        thumbnail = thumbnail,
        quantity = quantity,
        stock = stock,
        brand = brand,
        category = category,
        discountPercentage = discountPercentage,
        addedAt = System.currentTimeMillis()
    )
}
