package com.jvr.datovaschranka.dbhelper.dataStore
/*
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//https://androidgeek.co/how-to-use-datastore-preferences-in-kotlin-f1df16f17ac0
//https://stackoverflow.com/questions/44998051/cannot-create-an-instance-of-class-viewmodel
class DbViewModel (application: Application) : AndroidViewModel(application) {
    private val dataStore = DbDataStoreManager(application)

    val getUserTableHash = dataStore.getUserTableHash().asLiveData(Dispatchers.IO)

    fun setUserTableHash(userTableHash : Int) {
        viewModelScope.launch(Dispatchers.IO) { userTableHash
            dataStore.setUserTableHash(userTableHash)
        }
    }

}*/