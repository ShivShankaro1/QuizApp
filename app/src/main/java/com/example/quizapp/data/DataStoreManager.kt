package com.example.quizapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {
    private val USERNAME_KEY = stringPreferencesKey("username")

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    fun getUsername(): String? = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[USERNAME_KEY]
    }
}
