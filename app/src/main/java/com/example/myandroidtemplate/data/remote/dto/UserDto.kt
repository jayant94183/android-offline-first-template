package com.example.myandroidtemplate.data.remote.dto

import com.example.myandroidtemplate.data.local.entity.UserEntity

data class UserDto(
    val id: String,
    val email: String,
    val name: String
)