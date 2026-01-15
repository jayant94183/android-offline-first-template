package com.example.myandroidtemplate.data.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshCoordinator @Inject constructor() {

    private val mutex = Mutex()
    private var isRefreshing = false
    private var latestAccessToken: String? = null

    suspend fun refreshIfNeeded(
        refreshAction: suspend () -> String?
    ): String? {

        mutex.withLock {
            // If another refresh already completed, reuse it
            latestAccessToken?.let { return it }

            // If refresh in progress → wait
            if (isRefreshing) {
                while (isRefreshing) {
                    mutex.unlock()
                    kotlinx.coroutines.delay(10)
                    mutex.lock()
                }
                return latestAccessToken
            }

            // We are the refresher
            isRefreshing = true
        }

        val newToken = try {
            refreshAction()
        } finally {
            mutex.withLock {
                latestAccessToken = null
                isRefreshing = false
            }
        }

        mutex.withLock {
            latestAccessToken = newToken
        }

        return newToken
    }

    fun clear() {
        latestAccessToken = null
    }
}