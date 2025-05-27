package com.vishalpvijayan.thefreshly.di

import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.domain.repository.addUser.AddUserRepository
import com.vishalpvijayan.thefreshly.domain.repository.addUser.AddUserRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.login.UserRepository
import com.vishalpvijayan.thefreshly.domain.repository.login.UserRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.usecase.AddUserUsecase
import com.vishalpvijayan.thefreshly.domain.usecase.UserLoginUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    //user login

    @Provides
    @Singleton
    fun provideUserRepository(api: ApiServices): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    fun provideLoginUseCase(repository: UserRepository): UserLoginUsecase {
        return UserLoginUsecase(repository)
    }

    //add user


    @Provides
    @Singleton
    fun provideAddUserRepository(api: ApiServices): AddUserRepository {
        return AddUserRepositoryImpl(api)
    }

    @Provides
    fun provideAddUserUsecase(repository: AddUserRepository): AddUserUsecase {
        return AddUserUsecase(repository)
    }
}
