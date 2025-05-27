package com.vishalpvijayan.thefreshly.domain.repository.login

import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import retrofit2.Response

interface UserRepository {
    suspend fun loginUser(request: UserRequest): Response<UserResponse>
}