package com.vishalpvijayan.thefreshly.domain.repository.productCategory

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.paging.ProductCategoryPagingSource
import kotlin.collections.map
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import kotlinx.coroutines.flow.Flow


/*class ProductCategoryRepositoryImpl(
    private val apiService: ApiServices
) : ProductCategoryRepository {

    override suspend fun getProductCategories(): List<ProductCategory> {
        return apiService.getProductCategory().map { it.toDomain() }
    }
}*/


class ProductCategoryRepositoryImpl(
    private val apiService: ApiServices
) : ProductCategoryRepository {

    override fun getProductCategoriesPaged(): Flow<PagingData<ProductCategory>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ProductCategoryPagingSource(apiService) }
        ).flow
    }

}



