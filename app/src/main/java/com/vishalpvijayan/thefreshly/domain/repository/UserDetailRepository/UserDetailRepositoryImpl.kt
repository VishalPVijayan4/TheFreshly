package com.vishalpvijayan.thefreshly.domain.repository.UserDetailRepository

import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetail

class UserDetailRepositoryImpl(
    private val apiService: ApiServices
) : UserDetailRepository {
    override suspend fun getUserDetail(id: Int): UserDetail {
        return apiService.getUserDetail(id).toDomain()
    }
}