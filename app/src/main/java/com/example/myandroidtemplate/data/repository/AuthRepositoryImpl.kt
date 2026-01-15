package com.example.myandroidtemplate.data.repository

import com.example.myandroidtemplate.data.remote.api.AuthApi
import com.example.myandroidtemplate.data.remote.request.RefreshRequest
import com.example.myandroidtemplate.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val session: SessionManager
) : AuthRepository {

    override suspend fun refreshToken(): Boolean {
        val refresh = session.refreshToken() ?: return false
        return try {
            val res = api.refresh(RefreshRequest(refresh))
            session.saveTokens(
                res.accessToken,
                res.refreshToken
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
