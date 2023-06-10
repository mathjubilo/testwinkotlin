package com.example.testwinkotlin.data.local.api

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface IDataStoreApi {

    suspend fun saveBoolean(forKey: String, value: Boolean): Preferences
    suspend fun getBoolean(forKey: String): Boolean
    suspend fun saveString(forKey: String, value: String): Preferences
    suspend fun getString(forKey: String): String
}

class DataStoreApi
    @Inject constructor(
    private val application: Application,
    private val dataStore: DataStore<Preferences>
): IDataStoreApi {

    /*val context = application.applicationContext;
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "winStorage")*/

    override suspend fun saveBoolean(forKey: String, value: Boolean): Preferences {

        val key = booleanPreferencesKey(forKey)
        return /*context.*/dataStore.edit { winStorage ->
            winStorage[key] = value
        }
    }

    override suspend fun getBoolean(forKey: String): Boolean {

        val key = booleanPreferencesKey(forKey)
       return /*context.*/dataStore.data.map { preferences ->
            preferences[key] ?: false
        }.first()
    }

    override suspend fun saveString(forKey: String, value: String): Preferences {

        val key = stringPreferencesKey(forKey)
        return /*context.*/dataStore.edit { winStorage ->
            winStorage[key] = value
        }
    }

    override suspend fun getString(forKey: String): String {

        val key = stringPreferencesKey(forKey)
        return /*context.*/dataStore.data.map { preferences ->
            preferences[key] ?: ""
        }.first()
    }
}