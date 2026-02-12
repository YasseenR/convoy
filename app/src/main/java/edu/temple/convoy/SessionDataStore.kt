package edu.temple.convoy

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

object SessionDataStore {
    private val SESSION_KEY = stringPreferencesKey("session_key")
    private val USERNAME_KEY = stringPreferencesKey("username")

    suspend fun saveSession(
        context: Context,
        username: String,
        sessionKey: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[SESSION_KEY] = sessionKey
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun clearSession(context: Context) {
        context.dataStore.edit { it.clear() }
    }

    fun sessionKeyFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[SESSION_KEY]
        }

    fun usernameFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[USERNAME_KEY]
        }
}