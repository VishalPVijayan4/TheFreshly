package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetail
import com.vishalpvijayan.thefreshly.domain.usecase.GetUserDetailUseCase
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<UserDetail?>(null)
    val user: StateFlow<UserDetail?> = _user

    fun loadUserDetail(id: Int) {
        viewModelScope.launch {
            _user.value = getUserDetailUseCase(id)
        }
    }

    val userId: Flow<String> = dataStoreManager.getPreference(
        key = ConstantStrings.userId,
        clazz = String::class.java,
        defaultValue = "1"
    )
}