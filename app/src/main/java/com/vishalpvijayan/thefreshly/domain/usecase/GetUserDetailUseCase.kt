package com.vishalpvijayan.thefreshly.domain.usecase

import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetail
import com.vishalpvijayan.thefreshly.domain.repository.UserDetailRepository.UserDetailRepository

class GetUserDetailUseCase(
    private val repository: UserDetailRepository
) {
    suspend operator fun invoke(id: Int): UserDetail {
        return repository.getUserDetail(id)
    }
}