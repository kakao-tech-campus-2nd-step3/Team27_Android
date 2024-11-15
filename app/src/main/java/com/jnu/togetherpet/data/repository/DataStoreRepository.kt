package com.jnu.togetherpet.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //missing 여부 저장 key
    private val missingStatusKey = booleanPreferencesKey("missing_status")

    // petName, userName, imgUri, birth, isNeutral key
    private val petNameKey = stringPreferencesKey("pet_name")
    private val userNameKey = stringPreferencesKey("user_name")
    private val imgUriKey = stringPreferencesKey("img_uri")
    private val birthKey = longPreferencesKey("pet_birth")
    private val neutralKey = booleanPreferencesKey("pet_neutral")


    suspend fun saveMissingStatus(isMissing: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[missingStatusKey] = isMissing
            }
        }
    }

    suspend fun savePetName(petName: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[petNameKey] = petName
            }
        }
    }

    suspend fun saveUserName(userName: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[userNameKey] = userName
            }
        }
    }

    suspend fun savePetImgUri(imgUri: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[imgUriKey] = imgUri
            }
        }
    }

    suspend fun savePetBirth(birth: Long) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[birthKey] = birth
            }
        }
    }

    suspend fun savePetNeutral(isNeutral: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[neutralKey] = isNeutral
            }
        }
    }

    val missingStatus: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[missingStatusKey] ?: false
        }

    val petName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[petNameKey] ?: ""
        }

    val imgUri: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[imgUriKey] ?: ""
        }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[userNameKey] ?: ""
        }

    val petBirth: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[birthKey] ?: (0L)
        }

    val petIsNeutral: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[neutralKey] ?: (false)
        }
}