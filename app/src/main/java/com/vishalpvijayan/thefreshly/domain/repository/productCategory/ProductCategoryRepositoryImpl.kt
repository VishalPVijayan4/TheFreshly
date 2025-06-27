package com.vishalpvijayan.thefreshly.domain.repository.productCategory

import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import kotlin.collections.map
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory


class ProductCategoryRepositoryImpl(
    private val apiService: ApiServices
) : ProductCategoryRepository {

    override suspend fun getProductCategories(): List<ProductCategory> {
        return apiService.getProductCategory().map { it.toDomain() }
    }
}



