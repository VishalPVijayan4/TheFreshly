package com.vishalpvijayan.thefreshly.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vishalpvijayan.thefreshly.utils.ConstantStrings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = ConstantStrings.DATASTORE)
@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext val context : Context) {


    suspend fun <T> savePreference(key: String, value: T) {
        context.dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[stringPreferencesKey(key)] = value
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        }
    }

    fun <T> getPreference(key: String, clazz: Class<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            val result: Any? = when (clazz) {
                String::class.java -> preferences[stringPreferencesKey(key)] ?: defaultValue
                Boolean::class.java -> preferences[booleanPreferencesKey(key)] ?: defaultValue
                Int::class.java -> preferences[intPreferencesKey(key)] ?: defaultValue
                Float::class.java -> preferences[floatPreferencesKey(key)] ?: defaultValue
                Long::class.java -> preferences[longPreferencesKey(key)] ?: defaultValue
                else -> throw IllegalArgumentException("Unsupported return type")
            }
            @Suppress("UNCHECKED_CAST")
            result as T
        }
    }

    suspend fun clearPreference(key: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key)) // Assumes string key, update as needed
        }
    }

    suspend fun clearAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}
