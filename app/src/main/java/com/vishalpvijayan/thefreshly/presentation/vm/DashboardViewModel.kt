package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import com.vishalpvijayan.thefreshly.domain.usecase.AddUserUsecase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val dataStoreManager: DataStoreManager
):
    ViewModel(){





}