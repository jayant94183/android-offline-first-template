package com.example.myandroidtemplate.data.remote.dto

data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserDto
)