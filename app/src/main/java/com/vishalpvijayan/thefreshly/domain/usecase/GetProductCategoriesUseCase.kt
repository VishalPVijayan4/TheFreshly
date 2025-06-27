package com.vishalpvijayan.thefreshly.domain.usecase

import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.domain.repository.productCategory.ProductCategoryRepository


class GetProductCategoriesUseCase(
    private val repository: ProductCategoryRepository
) {
    suspend operator fun invoke(): List<ProductCategory> {
        return repository.getProductCategories()
    }
}
