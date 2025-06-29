package com.vishalpvijayan.thefreshly.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory


class ProductCategoryPagingSource(
    private val apiService: ApiServices
) : PagingSource<Int, ProductCategory>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductCategory> {
        val page = params.key ?: 1
        val pageSize = 10

        return try {
            val response = apiService.getProductCategory().map { it.toDomain() }
            val paged = response.chunked(pageSize).getOrNull(page - 1) ?: emptyList()

            LoadResult.Page(
                data = paged,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (paged.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductCategory>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}