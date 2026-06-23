package com.vishalpvijayan.thefreshly.data.mapper

import com.vishalpvijayan.thefreshly.Products

fun Products.toDomain(): Products {
    return Products(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail
    )
}