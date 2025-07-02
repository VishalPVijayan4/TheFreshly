package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductVM @Inject constructor(
    getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    val products: Flow<PagingData<Products>> = getProductsUseCase().cachedIn(viewModelScope)
}