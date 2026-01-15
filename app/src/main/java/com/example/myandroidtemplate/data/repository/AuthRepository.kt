package com.example.myandroidtemplate.data.repository

interface AuthRepository {
    suspend fun refreshToken(): Boolean
}