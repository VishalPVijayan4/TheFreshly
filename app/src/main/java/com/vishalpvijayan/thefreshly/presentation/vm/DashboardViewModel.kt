package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductCategoriesUseCase
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import com.vishalpvijayan.thefreshly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val dataStoreManager: DataStoreManager,
    private val apiServices: ApiServices
):
    ViewModel(){

    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState



    val categories: Flow<PagingData<ProductCategory>> =
        getProductCategoriesUseCase().cachedIn(viewModelScope)

    private val _curatedProducts = MutableStateFlow<List<Products>>(emptyList())
    val curatedProducts: StateFlow<List<Products>> = _curatedProducts

    fun loadCuratedProducts() {
        viewModelScope.launch {
            try {
                val firstCategory = apiServices.getProductCategory().firstOrNull()?.slug.orEmpty()
                if (firstCategory.isBlank()) {
                    _curatedProducts.value = emptyList()
                    return@launch
                }

                val response = apiServices.getProductsByCategory(
                    category = firstCategory,
                    limit = 4,
                    skip = 0
                )
                _curatedProducts.value = response.products.take(4)
            } catch (e: Exception) {
                _curatedProducts.value = emptyList()
            }
        }
    }


    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> get() = _username

    private val _productCategories = MutableLiveData<Resource<List<String>>>()
    val productCategories: LiveData<Resource<List<String>>> = _productCategories


    val usernameFlow: Flow<String> = dataStoreManager.getPreference(
        key = ConstantStrings.username,
        clazz = String::class.java,
        defaultValue = "-"
    )

    fun setLoadState(state: LoadState) {
        _loadState.value = state
    }

    val userId : Flow<Int> = dataStoreManager.getPreference("user_id", Int::class.java, -1)
}