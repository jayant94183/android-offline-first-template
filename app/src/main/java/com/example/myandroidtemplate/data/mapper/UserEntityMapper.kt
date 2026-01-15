package com.example.myandroidtemplate.data.mapper

import com.example.myandroidtemplate.data.local.entity.UserEntity
import com.example.myandroidtemplate.domain.model.User

fun UserEntity.toDomain(): User =
    User(
        id = id,
        email = email,
        name = name
    )