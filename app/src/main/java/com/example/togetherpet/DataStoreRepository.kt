//PreferencesDataStore 사용
package com.example.togetherpet

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //missing 여부 저장 key
    private val missingStatusKey = booleanPreferencesKey("missing_status")

    suspend fun saveMissingStatus(isMissing: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[missingStatusKey] = isMissing
            }
        }
    }

    val missingStatus: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[missingStatusKey] ?: false
        }
}