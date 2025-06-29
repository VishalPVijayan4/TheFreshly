package com.vishalpvijayan.thefreshly.domain.usecase

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.domain.repository.productCategory.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow


/*class GetProductCategoriesUseCase(
    private val repository: ProductCategoryRepository
) {
    suspend operator fun invoke(): List<ProductCategory> {
        return repository.getProductCategories()
    }
}*/


class GetProductCategoriesUseCase(
    private val repository: ProductCategoryRepository
) {
    operator fun invoke(): Flow<PagingData<ProductCategory>> {
        return repository.getProductCategoriesPaged()
    }
}
