package com.example.myandroidtemplate.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(
    name = "session"
)

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.sessionDataStore

    private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        dataStore.edit {
            it[KEY_ACCESS_TOKEN] = accessToken
            it[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun accessToken(): String? =
        dataStore.data.first()[KEY_ACCESS_TOKEN]

    suspend fun refreshToken(): String? =
        dataStore.data.first()[KEY_REFRESH_TOKEN]

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }
}
