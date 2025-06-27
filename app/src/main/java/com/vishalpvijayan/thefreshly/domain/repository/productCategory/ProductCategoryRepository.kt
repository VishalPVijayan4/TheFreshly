package com.vishalpvijayan.thefreshly.domain.repository.productCategory

import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory

interface ProductCategoryRepository {
    suspend fun getProductCategories(): List<ProductCategory>
}