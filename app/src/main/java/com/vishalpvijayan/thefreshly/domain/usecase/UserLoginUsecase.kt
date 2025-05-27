package com.vishalpvijayan.thefreshly.domain.usecase

import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.domain.repository.login.UserRepository
import com.vishalpvijayan.thefreshly.utils.Resource

class UserLoginUsecase(private val repository: UserRepository) {
    suspend operator fun invoke(request: UserRequest): Resource<UserResponse> {
        return try {
            val response = repository.loginUser(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error("Error: ${response.message()}", response.code())
            }

        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }

    }
}