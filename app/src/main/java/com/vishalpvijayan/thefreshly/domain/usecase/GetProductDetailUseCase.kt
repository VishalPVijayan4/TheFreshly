package com.vishalpvijayan.thefreshly.domain.usecase

import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.domain.repository.productDetail.ProductDetailRepository


class GetProductDetailUseCase(
    private val repository: ProductDetailRepository
) {
    suspend operator fun invoke(id: Int): ProductDetail {
        return repository.getProductDetail(id)
    }
}