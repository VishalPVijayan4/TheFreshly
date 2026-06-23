package com.vishalpvijayan.thefreshly.domain.repository.productByCategory

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.data.paging.ProductPagingSourceByCategory
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import kotlinx.coroutines.flow.Flow

class ProductRepositoryByCategoryImpl(private val apiService: ApiServices): ProductRepositoryByCategory {
    override fun getProducts(): Flow<PagingData<Products>> {
        TODO("Not yet implemented")
    }

    override fun getProductsByCategory(category: String): Flow<PagingData<Products>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductPagingSourceByCategory(apiService, category)
            }
        ).flow
    }


}