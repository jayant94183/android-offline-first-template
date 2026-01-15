package com.example.myandroidtemplate.data.remote.dto

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresInMillis: Long
)