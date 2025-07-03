package com.vishalpvijayan.thefreshly.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    private val _product = MutableStateFlow<ProductDetail?>(null)
    val product: StateFlow<ProductDetail?> = _product

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                _product.value = getProductDetailUseCase(id)
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "Failed to load product detail", e)
            }
        }
    }

}