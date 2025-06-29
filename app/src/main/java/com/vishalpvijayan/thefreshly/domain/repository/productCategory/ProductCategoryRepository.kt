package com.vishalpvijayan.thefreshly.domain.repository.productCategory

import androidx.paging.PagingData
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import kotlinx.coroutines.flow.Flow

/*interface ProductCategoryRepository {
    suspend fun getProductCategories(): List<ProductCategory>
}*/

interface ProductCategoryRepository {
    fun getProductCategoriesPaged(): Flow<PagingData<ProductCategory>>
}