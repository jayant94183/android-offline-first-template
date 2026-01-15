package com.example.myandroidtemplate.data.repository

import com.example.myandroidtemplate.data.local.datasource.UserLocalDataSource
import com.example.myandroidtemplate.data.mapper.toDomain
import com.example.myandroidtemplate.data.mapper.toEntity
import com.example.myandroidtemplate.data.remote.datasource.UserRemoteDataSource
import com.example.myandroidtemplate.domain.model.User
import com.example.myandroidtemplate.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val local: UserLocalDataSource,
    private val remote: UserRemoteDataSource,
    private val sessionManager: SessionManager
) : UserRepository {

    override fun observeUser(): Flow<User?> =
        local.observeUser().map { it?.toDomain() }

    override suspend fun login(email: String, password: String) {
        val userDto = remote.login(email, password)
        local.saveUser(userDto.toEntity())
    }

    override suspend fun signup(email: String, password: String, name: String) {
        val userDto = remote.signup(email, password, name)
        local.saveUser(userDto.toEntity())
    }

    override suspend fun logout() {
        sessionManager.clearTokens()
        local.clearUser()
    }
}