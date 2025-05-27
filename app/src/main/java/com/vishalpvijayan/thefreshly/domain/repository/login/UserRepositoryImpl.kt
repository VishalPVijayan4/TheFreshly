package com.vishalpvijayan.thefreshly.domain.repository.login

import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiServices
) : UserRepository {
    override suspend fun loginUser(request: UserRequest): Response<UserResponse> {
        return api.loginUser(request)
    }
}
