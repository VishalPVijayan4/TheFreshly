package com.vishalpvijayan.thefreshly.di


import com.vishalpvijayan.thefreshly.data.local.AppDatabase
import com.vishalpvijayan.thefreshly.data.repository.SupportRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.support.SupportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupportModule {

    @Provides
    @Singleton
    fun provideSupportMessageDao(database: AppDatabase) = database.supportMessageDao()

    @Provides
    @Singleton
    fun provideSupportRepository(
        repositoryImpl: SupportRepositoryImpl
    ): SupportRepository = repositoryImpl
}
