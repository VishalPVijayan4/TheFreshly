package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.domain.usecase.UserLoginUsecase
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import com.vishalpvijayan.thefreshly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val loginUseCase: UserLoginUsecase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<UserResponse>>()
    val loginState: LiveData<Resource<UserResponse>> = _loginState

    val isLoggedIn: Flow<Boolean> = dataStoreManager.getPreference(
        ConstantStrings.isLoggedIn,
        Boolean::class.java,
        false
    )

    fun login(username: String, password: String) {
        if (_loginState.value is Resource.Loading) return

        viewModelScope.launch {
            _loginState.value = Resource.Loading
            when (val result = loginUseCase(UserRequest(username, password))) {
                is Resource.Success -> {
                    val user = result.data
                    // Persist the complete session before exposing success to the UI. This avoids
                    // losing authentication if the process is stopped immediately after navigation.
                    dataStoreManager.saveUserSession(
                        userId = user.id?.toInt() ?: 1,
                        username = user.username.orEmpty(),
                        token = user.accessToken.orEmpty()
                    )
                    _loginState.value = result
                }
                is Resource.Error -> _loginState.value = result
                Resource.Loading -> Unit
            }
        }
    }
}
