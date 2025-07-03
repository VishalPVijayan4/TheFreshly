package com.vishalpvijayan.thefreshly.domain.repository.productDetail

import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail


class ProductDetailRepositoryImpl(
    private val apiService: ApiServices
) : ProductDetailRepository {
    override suspend fun getProductDetail(id: Int): ProductDetail {
        return apiService.getProductDetail(id).toDomain()
    }
}