package com.example.myandroidtemplate.di

import com.example.myandroidtemplate.data.repository.AuthRepository
import com.example.myandroidtemplate.data.repository.AuthRepositoryImpl
import com.example.myandroidtemplate.data.repository.UserRepository
import com.example.myandroidtemplate.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepo(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}