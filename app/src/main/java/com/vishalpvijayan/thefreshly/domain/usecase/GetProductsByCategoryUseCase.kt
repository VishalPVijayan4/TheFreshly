package com.vishalpvijayan.thefreshly.domain.usecase

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.domain.repository.productByCategory.ProductRepositoryByCategory
import kotlinx.coroutines.flow.Flow

class GetProductsByCategoryUseCase(
    private val repository: ProductRepositoryByCategory
) {
    operator fun invoke(category: String): Flow<PagingData<Products>> {
        return repository.getProductsByCategory(category)
    }
}
