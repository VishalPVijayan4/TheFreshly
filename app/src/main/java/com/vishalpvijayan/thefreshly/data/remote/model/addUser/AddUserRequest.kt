package com.vishalpvijayan.thefreshly.data.remote.model.addUser

data class AddUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val phone: Int,
)
