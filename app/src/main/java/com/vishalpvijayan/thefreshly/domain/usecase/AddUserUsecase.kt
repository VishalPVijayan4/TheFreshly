package com.vishalpvijayan.thefreshly.domain.usecase

import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse
import com.vishalpvijayan.thefreshly.domain.repository.addUser.AddUserRepository


class AddUserUsecase(private val repository: AddUserRepository) {
    suspend operator fun invoke(request: AddUserRequest): AddUserResponse {
        return repository.addUser(request)
    }
}