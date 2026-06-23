package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductsByCategoryUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject





@HiltViewModel
class SingleCategoryProductVM @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    fun getProductsByCategory(category: String): Flow<PagingData<Products>> {
        return getProductsByCategoryUseCase(category).cachedIn(viewModelScope)
    }

    private val _category = MutableStateFlow<String?>(null)

    val products: Flow<PagingData<Products>> = _category
        .filterNotNull()
        .flatMapLatest { category ->
            getProductsByCategoryUseCase(category)
        }.cachedIn(viewModelScope)

    fun setCategory(category: String) {
        _category.value = category
    }

//    val products: Flow<PagingData<Products>> = getProductsByCategoryUseCase("smartphones").cachedIn(viewModelScope)
}