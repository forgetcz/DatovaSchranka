@file:Suppress("UnnecessaryVariable")

package com.jvr.common.lib

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class AppSettings private constructor(applicationContext : Context) {
    companion object {
        @Volatile
        private var INSTANCE: AppSettings? = null

        fun getInstance(context: Context): AppSettings =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppSettings(context).also { INSTANCE = it }
            }
    }

    private var sharedPreferences: SharedPreferences

    init {
        //val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val mainKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "preferences",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        /*sharedPreferences = EncryptedSharedPreferences.create(applicationContext,
            // passing a file name to share a preferences
            "preferences",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )*/
    }

    fun getKey(keyName: String): String? {
        val keyValue = sharedPreferences.getString(keyName, "")
        return keyValue
    }

    fun setKey(keyName: String, keyValue: String) {
        sharedPreferences.edit().putString(keyName, keyValue).apply()
    }
}
