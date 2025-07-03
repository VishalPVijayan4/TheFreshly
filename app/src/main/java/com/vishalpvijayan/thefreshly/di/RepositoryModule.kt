package com.vishalpvijayan.thefreshly.di

import com.vishalpvijayan.thefreshly.data.remote.ApiServices
import com.vishalpvijayan.thefreshly.domain.repository.AllProducts.ProductRepository
import com.vishalpvijayan.thefreshly.domain.repository.AllProducts.ProductRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.UserDetailRepository.UserDetailRepository
import com.vishalpvijayan.thefreshly.domain.repository.UserDetailRepository.UserDetailRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.addUser.AddUserRepository
import com.vishalpvijayan.thefreshly.domain.repository.addUser.AddUserRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.login.UserRepository
import com.vishalpvijayan.thefreshly.domain.repository.login.UserRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.productByCategory.ProductRepositoryByCategory
import com.vishalpvijayan.thefreshly.domain.repository.productByCategory.ProductRepositoryByCategoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.productCategory.ProductCategoryRepository
import com.vishalpvijayan.thefreshly.domain.repository.productCategory.ProductCategoryRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.repository.productDetail.ProductDetailRepository
import com.vishalpvijayan.thefreshly.domain.repository.productDetail.ProductDetailRepositoryImpl
import com.vishalpvijayan.thefreshly.domain.usecase.AddUserUsecase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductCategoriesUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductDetailUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductsByCategoryUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.GetProductsUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.GetUserDetailUseCase
import com.vishalpvijayan.thefreshly.domain.usecase.UserLoginUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /*                USER LOGIN               */
    @Provides
    @Singleton
    fun provideUserRepository(api: ApiServices): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    fun provideLoginUseCase(repository: UserRepository): UserLoginUsecase {
        return UserLoginUsecase(repository)
    }



    /*                   ADD USERS                */
    @Provides
    @Singleton
    fun provideAddUserRepository(api: ApiServices): AddUserRepository {
        return AddUserRepositoryImpl(api)
    }

    @Provides
    fun provideAddUserUsecase(repository: AddUserRepository): AddUserUsecase {
        return AddUserUsecase(repository)
    }


    /*            GET PRODUCT CATEGORY            */
    @Provides
    @Singleton
    fun provideProductCategoryRepository(apiService: ApiServices): ProductCategoryRepository {
        return ProductCategoryRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductCategoryUseCase(repository: ProductCategoryRepository): GetProductCategoriesUseCase {
        return GetProductCategoriesUseCase(repository)
    }


    /*         GET ALL PRODUCT           */
    @Provides
    @Singleton
    fun provideProductRepository(apiService: ApiServices): ProductRepository {
        return ProductRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductUseCase(repository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideProductByCategoryRepository(apiService: ApiServices): ProductRepositoryByCategory {
        return ProductRepositoryByCategoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductByCategoryUsecase(repository: ProductRepositoryByCategory): GetProductsByCategoryUseCase {
        return GetProductsByCategoryUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideProductDetailRepository(apiService: ApiServices): ProductDetailRepository {
        return ProductDetailRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductDetailUseCase(repository: ProductDetailRepository): GetProductDetailUseCase {
        return GetProductDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUserDetailRepository(apiService: ApiServices): UserDetailRepository {
        return UserDetailRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideUserDetailUseCase(repository: UserDetailRepository): GetUserDetailUseCase {
        return GetUserDetailUseCase(repository)
    }



}
