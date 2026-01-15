package com.example.myandroidtemplate.data.mapper

import com.example.myandroidtemplate.data.local.entity.UserEntity
import com.example.myandroidtemplate.data.remote.dto.UserDto

fun UserDto.toEntity(): UserEntity =
    UserEntity(
        id = id,
        email = email,
        name = name
    )