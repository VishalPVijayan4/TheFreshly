package com.vishalpvijayan.thefreshly.utils

import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory

sealed class CategoryListItem {
    data class CategoryItem(val category: ProductCategory) : CategoryListItem()
    data class StaticBanner(val imageRes: Int) : CategoryListItem()
}