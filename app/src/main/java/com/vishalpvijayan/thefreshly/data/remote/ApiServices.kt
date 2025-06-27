package com.vishalpvijayan.thefreshly.data.remote

import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
//    @POST("user/login")
//    suspend fun getUsers(@Query("page") page: Int): UserResponse

    @POST("/user/login")
    suspend fun loginUser(@Body request: UserRequest): Response<UserResponse>

    @POST("/users/add")
    suspend fun addUser(@Body request: AddUserRequest): AddUserResponse

    @GET("products/categories")
    suspend fun getProductCategory(): List<ProductCategory>




}