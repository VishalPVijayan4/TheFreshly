package com.vishalpvijayan.thefreshly.domain.repository.addUser

import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse

interface AddUserRepository {
    suspend fun addUser(request: AddUserRequest): AddUserResponse
}