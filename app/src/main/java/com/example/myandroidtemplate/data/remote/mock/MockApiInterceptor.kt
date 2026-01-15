package com.example.myandroidtemplate.data.remote.mock

import com.example.myandroidtemplate.utils.FeatureFlags
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!FeatureFlags.USE_MOCK_API) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()
        val path = request.url.encodedPath

        // Optional artificial delay
        if (FeatureFlags.MOCK_API_DELAY_MS > 0) {
            Thread.sleep(FeatureFlags.MOCK_API_DELAY_MS)
        }

        val json = when {
            path.contains("/auth/login") -> mockLogin()
            path.contains("/auth/signup") -> mockSignup()
            path.contains("/auth/refresh") -> mockRefresh()
            else -> null
        }

        return if (json != null) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK (mock)")
                .body(
                    json.toResponseBody(
                        "application/json".toMediaType()
                    )
                )
                .build()
        } else {
            chain.proceed(request)
        }
    }

    // -----------------------------
    // Mock JSON responses
    // -----------------------------

    private fun mockLogin(): String =
        """
    {
      "token": "mock_access_token",
      "refreshToken": "mock_refresh_token",
      "user": {
        "id": "1",
        "email": "mock@example.com",
        "name": "Mock User"
      }
    }
    """.trimIndent()

    private fun mockSignup(): String =
        """
    {
      "token": "mock_access_token",
      "refreshToken": "mock_refresh_token",
      "user": {
        "id": "2",
        "email": "newmock@example.com",
        "name": "New Mock User"
      }
    }
    """.trimIndent()

    private fun mockRefresh(): String =
        """
        {
          "accessToken": "mock_new_access_token",
          "refreshToken": "mock_refresh_token",
          "expiresInMillis": 3600000
        }
        """.trimIndent()
}