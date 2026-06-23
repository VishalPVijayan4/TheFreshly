package com.vishalpvijayan.thefreshly.domain.usecase

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.domain.repository.AllProducts.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<PagingData<Products>> = repository.getProducts()
}