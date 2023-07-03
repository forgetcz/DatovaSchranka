package com.jvr.datovaschranka.dbhelper.dataStore
/*
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map


class DataStoreManager1(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")

    companion object {
        val userTable = stringPreferencesKey("UserTable")
    }

    suspend fun clearDataStore() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveToDataStore(userTableStr: String) {
        context.dataStore.edit { preference ->
            preference[userTable] = userTableStr
        }
    }

    fun getUserTable() = context.dataStore.data.map {
        preferences -> preferences[userTable]
    }

    fun getUserTable1(pref : String) = context.dataStore.data.map {
            preferences -> preferences[stringPreferencesKey(pref)] ?: ""
    }

}*/