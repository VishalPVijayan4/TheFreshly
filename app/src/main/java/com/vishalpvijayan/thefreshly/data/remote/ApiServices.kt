package com.vishalpvijayan.thefreshly.data.remote

import com.vishalpvijayan.thefreshly.GetAllProducts
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.ProductDetailDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetailDto
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.addUser.AddUserResponse
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserRequest
import com.vishalpvijayan.thefreshly.data.remote.model.login.UserResponse
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @POST("/user/login")
    suspend fun loginUser(@Body request: UserRequest): Response<UserResponse>

    @POST("/users/add")
    suspend fun addUser(@Body request: AddUserRequest): AddUserResponse

    @GET("products/categories")
    suspend fun getProductCategory(): List<ProductCategory>

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): GetAllProducts


    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): GetAllProducts



    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): ProductDetailDto


    @GET("users/{id}")
    suspend fun getUserDetail(@Path("id") id: Int): UserDetailDto

}