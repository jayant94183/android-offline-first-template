package com.example.myandroidtemplate.data.remote

import com.example.myandroidtemplate.data.auth.TokenRefreshCoordinator
import com.example.myandroidtemplate.data.local.db.UserDao
import com.example.myandroidtemplate.data.remote.api.AuthApi
import com.example.myandroidtemplate.data.remote.request.RefreshRequest
import com.example.myandroidtemplate.di.RefreshApi
import com.example.myandroidtemplate.utils.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
    @RefreshApi private val refreshApi: AuthApi,
    private val refreshCoordinator: TokenRefreshCoordinator,
    private val userDao: UserDao
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //  Attach access token if available
        val originalRequest = chain.request()
        val requestWithToken = runBlocking {
            sessionManager.accessToken()?.let { token ->
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } ?: originalRequest
        }

        val response = chain.proceed(requestWithToken)

        //  If not unauthorized, return immediately
        if (response.code != 401) {
            return response
        }

        // Important: close the original response before retrying
        response.close()

        //  Attempt refresh (single-flight via coordinator)
        val newAccessToken = runBlocking {
            refreshCoordinator.refreshIfNeeded {
                try {
                    val refreshToken =
                        sessionManager.refreshToken() ?: return@refreshIfNeeded null

                    val refreshResponse =
                        refreshApi.refresh(RefreshRequest(refreshToken))

                    // Token-only storage
                    sessionManager.saveTokens(
                        accessToken = refreshResponse.accessToken,
                        refreshToken = refreshResponse.refreshToken
                    )

                    refreshResponse.accessToken
                } catch (e: Exception) {
                    null
                }
            }
        }

        //  Retry request if refresh succeeded
        return if (newAccessToken != null) {
            chain.proceed(
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            )
        } else {

            //  Global logout (SSOT)
            runBlocking {
                refreshCoordinator.clear()
                sessionManager.clearTokens()
                userDao.clear()
            }
            response
        }
    }
}
