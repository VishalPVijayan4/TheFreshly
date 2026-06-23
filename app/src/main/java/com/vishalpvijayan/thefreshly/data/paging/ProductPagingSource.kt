package com.vishalpvijayan.thefreshly.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.remote.ApiServices

class ProductPagingSource(
    private val apiService: ApiServices
) : PagingSource<Int, Products>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Products> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize

            val response = apiService.getProducts(limit, page)
            val products = response.products.map { it.toDomain() }

            LoadResult.Page(
                data = products,
                prevKey = if (page == 0) null else page - limit,
                nextKey = if (products.isEmpty()) null else page + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Products>): Int? {
        return state.anchorPosition
    }
}
