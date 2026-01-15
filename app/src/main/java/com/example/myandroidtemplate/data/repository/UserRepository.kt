package com.example.myandroidtemplate.data.repository

import com.example.myandroidtemplate.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeUser(): Flow<User?>

    suspend fun login(email: String, password: String)

    suspend fun signup(email: String, password: String, name: String)

    suspend fun logout()
}
