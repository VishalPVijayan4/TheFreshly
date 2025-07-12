package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.domain.usecase.AddUserUsecase
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
    private val dataStoreManager: DataStoreManager
):
    ViewModel(){

    val categories: Flow<PagingData<ProductCategory>> =
        getProductCategoriesUseCase().cachedIn(viewModelScope)


    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> get() = _username

    private val _productCategories = MutableLiveData<Resource<List<String>>>()
    val productCategories: LiveData<Resource<List<String>>> = _productCategories


    val usernameFlow: Flow<String> = dataStoreManager.getPreference(
        key = ConstantStrings.username,
        clazz = String::class.java,
        defaultValue = "-"
    )

    val userId : Flow<Int> = dataStoreManager.getPreference("user_id", Int::class.java, -1)






}