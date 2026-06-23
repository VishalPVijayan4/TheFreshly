package com.vishalpvijayan.thefreshly.domain.repository.productByCategory

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import kotlinx.coroutines.flow.Flow

interface ProductRepositoryByCategory {
    fun getProducts(): Flow<PagingData<Products>>
    fun getProductsByCategory(category: String): Flow<PagingData<Products>>
}
