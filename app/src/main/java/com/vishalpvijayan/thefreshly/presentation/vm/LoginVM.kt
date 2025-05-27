package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.domain.usecase.UserLoginUsecase
import com.vishalpvijayan.thefreshly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor(
    private val loginUseCase: UserLoginUsecase
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<UserResponse>>()
    val loginState: LiveData<Resource<UserResponse>> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            val result = loginUseCase(UserRequest(username, password))
            _loginState.value = result

            /*try {
                val result = loginUseCase(UserRequest(username, password))
                _loginResult.value = result
                Log.e("API_CALL", "Login Success: $result")
            } catch (e: Exception) {
                Log.e("API_CALL", "Login Failed: ${e.localizedMessage}")
            }*/
        }
    }
}
