package com.jvr.datovaschranka.dbhelper.dataStore
/*
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.jvr.datovaschranka.dbhelper.dataStore.DataStoreRepository.PreferencesKeys.REMEMBER
import com.jvr.datovaschranka.dbhelper.dataStore.DataStoreRepository.PreferencesKeys.USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    data class UserPreferences(
        val username: String,
        val remember: Boolean
    )

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val REMEMBER = booleanPreferencesKey("remember")
    }

    suspend fun saveToDataStore(username: String, remember: Boolean) {
        dataStore.edit { preference ->
            preference[USERNAME] = username
            preference[REMEMBER] = remember
        }
    }

    val readFromDataStore : Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStoreRepository", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val username = preference[USERNAME] ?: ""
            val remember = preference[REMEMBER] ?: false
            UserPreferences(username, remember)
        }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun removeUsername() {
        dataStore.edit { preference ->
            preference.remove(USERNAME)
        }
    }
}*/