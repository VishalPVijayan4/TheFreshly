package com.vishalpvijayan.thefreshly.domain.repository.UserDetailRepository

import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetail

interface UserDetailRepository {
    suspend fun getUserDetail(id: Int): UserDetail
}