package com.vishalpvijayan.thefreshly.data.mapper

import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory


fun ProductCategory.toDomain(): ProductCategory {
    return ProductCategory(
        slug = slug,
        name = name,
        url = url
    )
}
