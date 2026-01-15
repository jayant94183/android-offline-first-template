package com.example.myandroidtemplate.data.remote.datasource

import com.example.myandroidtemplate.data.remote.api.AuthApi
import com.example.myandroidtemplate.data.remote.dto.UserDto
import com.example.myandroidtemplate.data.remote.request.LoginRequest
import com.example.myandroidtemplate.data.remote.request.SignupRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun login(
        email: String,
        password: String
    ): UserDto {
        return authApi
            .login(LoginRequest(email, password))
            .user
    }

    suspend fun signup(
        email: String,
        password: String,
        name: String
    ): UserDto {
        return authApi
            .signup(SignupRequest(name, email, password))
            .user
    }
}