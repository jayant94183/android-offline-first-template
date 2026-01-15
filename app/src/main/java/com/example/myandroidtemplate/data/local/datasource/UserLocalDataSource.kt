package com.example.myandroidtemplate.data.local.datasource

import com.example.myandroidtemplate.data.local.db.UserDao
import com.example.myandroidtemplate.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) {

    fun observeUser(): Flow<UserEntity?> =
        userDao.observeUser()

    suspend fun saveUser(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun clearUser() {
        userDao.clear()
    }
}