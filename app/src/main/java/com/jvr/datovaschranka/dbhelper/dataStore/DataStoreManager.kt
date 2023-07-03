package com.jvr.datovaschranka.dbhelper.dataStore
/*

   // Data Store
    implementation "androidx.datastore:datastore-preferences:1.0.0"

    //lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.5.1'
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"




import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//https://gist.github.com/shibbirweb/d4e028e47f960d6d2308229ec67459c8
//https://androidgeek.co/how-to-use-datastore-preferences-in-kotlin-f1df16f17ac0

val Context.userSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class DataStoreManager {

    suspend fun saveValue(context: Context, key: String, value: String) {

        val wrappedKey = stringPreferencesKey(key)
        context.userSettingsDataStore.edit {
            it[wrappedKey] = value
        }

    }

    suspend fun saveValue(context: Context, key: String, value: Int) {

        val wrappedKey = intPreferencesKey(key)
        context.userSettingsDataStore.edit {
            it[wrappedKey] = value
        }

    }

    suspend fun saveValue(context: Context, key: String, value: Double) {

        val wrappedKey = doublePreferencesKey(key)
        context.userSettingsDataStore.edit {
            it[wrappedKey] = value
        }

    }

    suspend fun saveValue(context: Context, key: String, value: Long) {

        val wrappedKey = longPreferencesKey(key)
        context.userSettingsDataStore.edit {
            it[wrappedKey] = value
        }

    }

    suspend fun saveValue(context: Context, key: String, value: Boolean) {

        val wrappedKey = booleanPreferencesKey(key)
        context.userSettingsDataStore.edit {
            it[wrappedKey] = value
        }

    }

    suspend fun getStringValue(context: Context, key: String, default: String = ""): String {
        val wrappedKey = stringPreferencesKey(key)
        val valueFlow: Flow<String> = context.userSettingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getIntValue(context: Context, key: String, default: Int = 0): Int {
        val wrappedKey = intPreferencesKey(key)
        val valueFlow: Flow<Int> = context.userSettingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getDoubleValue(context: Context, key: String, default: Double = 0.0): Double {
        val wrappedKey = doublePreferencesKey(key)
        val valueFlow: Flow<Double> = context.userSettingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getLongValue(context: Context, key: String, default: Long = 0L): Long {
        val wrappedKey = longPreferencesKey(key)
        val valueFlow: Flow<Long> = context.userSettingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getBooleanValue(context: Context, key: String, default: Boolean = false): Boolean {
        val wrappedKey = booleanPreferencesKey(key)
        val valueFlow: Flow<Boolean> = context.userSettingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

}*/