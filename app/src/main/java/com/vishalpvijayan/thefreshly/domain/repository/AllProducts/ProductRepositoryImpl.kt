package com.vishalpvijayan.thefreshly.domain.repository.AllProducts

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.data.paging.ProductPagingSource
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val apiService: ApiServices
) : ProductRepository {

    override fun getProducts(): Flow<PagingData<Products>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ProductPagingSource(apiService) }
        ).flow
    }
}