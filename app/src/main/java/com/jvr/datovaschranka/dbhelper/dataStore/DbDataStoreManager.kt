package com.jvr.datovaschranka.dbhelper.dataStore
/*
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//https://dev.to/ethand91/android-compose-datastore-tutorial-3bnl
//https://androidgeek.co/how-to-use-datastore-preferences-in-kotlin-f1df16f17ac0

class DbDataStoreManager (context: Context){

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DATABASE_KEYS")
    private val dataStore = context.dataStore

    companion object {
        val userTableHash = intPreferencesKey("USER_TABLE_HASH")
        val tableHistoryVersion = intPreferencesKey("TABLE_HISTORY_VERSION")
    }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun setUserTableHash(iTableHistoryHash: Int) {
        dataStore.edit { preferences ->
            preferences[userTableHash] = iTableHistoryHash
        }
    }

    fun getUserTableHash(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[userTableHash] ?: 0
                uiMode
            }
    }

    suspend fun setUserTableVersion(iTableHistoryHash: Int) {
        dataStore.edit { preferences ->
            preferences[tableHistoryVersion] = iTableHistoryHash
        }
    }

    fun getUserTableVersion(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[tableHistoryVersion] ?: 0
                uiMode
            }
    }
}*/