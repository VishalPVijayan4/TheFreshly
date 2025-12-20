package com.vishalpvijayan.thefreshly.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
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
    val addedAt: Long = System.currentTimeMillis()
)
