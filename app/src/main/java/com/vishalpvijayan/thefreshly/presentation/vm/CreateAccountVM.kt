package com.vishalpvijayan.thefreshly.presentation.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse
import com.vishalpvijayan.thefreshly.domain.usecase.AddUserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAccountVM @Inject constructor(
    private val addUserUseCase: AddUserUsecase
) : ViewModel() {

    private val _addUserResult = MutableLiveData<AddUserResponse>()
    val addUserResult: LiveData<AddUserResponse> = _addUserResult

    fun addUser(username: String, password: String, email: String,phone: Int) {
        viewModelScope.launch {
            try {
                val result = addUserUseCase(AddUserRequest(username,password, email,phone))
                _addUserResult.value = result
                Log.e("API_CALL", "User Added Successfully: $result")
            } catch (e: Exception) {
                Log.e("API_CALL", "User Added Failed: ${e.localizedMessage}")
            }
        }
    }
}
