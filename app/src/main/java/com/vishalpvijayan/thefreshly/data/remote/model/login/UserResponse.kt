package com.vishalpvijayan.thefreshly.data.remote.model.login

import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("accessToken")
    var accessToken: String? = null
    @SerializedName("refreshToken")
    var refreshToken: String? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("image")
    var image: String? = null
}