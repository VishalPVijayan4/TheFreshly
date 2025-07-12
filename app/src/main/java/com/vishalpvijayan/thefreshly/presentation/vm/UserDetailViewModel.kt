package com.vishalpvijayan.thefreshly.presentation.vm

import android.util.Log
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

    val userId : Flow<Int> = dataStoreManager.getPreference(ConstantStrings.userId, Int::class.java, -1)

    fun loadUserDetail(id: Int) {
        viewModelScope.launch {
            try {
                val userDetail = getUserDetailUseCase(id)
                _user.value = userDetail
                Log.d("UserDebug", "User loaded: $userDetail")
            } catch (e: Exception) {
                Log.e("UserDebug", "Failed to load user", e)
            }
        }
    }



    /*val userId: Flow<Int> = dataStoreManager.getPreference(
        key = ConstantStrings.userId,
        clazz = String::class.java,
        defaultValue = 1
    )*/
}