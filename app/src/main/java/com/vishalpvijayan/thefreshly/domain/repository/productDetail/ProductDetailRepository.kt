package com.vishalpvijayan.thefreshly.domain.repository.productDetail

import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail

interface ProductDetailRepository {
    suspend fun getProductDetail(id: Int): ProductDetail

}