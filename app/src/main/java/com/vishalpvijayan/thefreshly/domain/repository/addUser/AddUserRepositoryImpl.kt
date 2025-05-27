package com.vishalpvijayan.thefreshly.domain.repository.addUser

import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse
import javax.inject.Inject

class AddUserRepositoryImpl @Inject constructor(private val api: ApiServices) : AddUserRepository {
    override suspend fun addUser(request: AddUserRequest): AddUserResponse {
        return api.addUser(request)
    }
}

