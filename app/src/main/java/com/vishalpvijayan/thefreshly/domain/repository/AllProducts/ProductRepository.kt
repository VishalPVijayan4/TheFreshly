package com.vishalpvijayan.thefreshly.domain.repository.AllProducts

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<PagingData<Products>>
}