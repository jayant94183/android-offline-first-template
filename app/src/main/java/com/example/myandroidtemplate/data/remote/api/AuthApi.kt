package com.example.myandroidtemplate.data.remote.api

import com.example.myandroidtemplate.data.remote.dto.AuthResponse
import com.example.myandroidtemplate.data.remote.dto.RefreshResponse
import com.example.myandroidtemplate.data.remote.request.LoginRequest
import com.example.myandroidtemplate.data.remote.request.RefreshRequest
import com.example.myandroidtemplate.data.remote.request.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): RefreshResponse
}

