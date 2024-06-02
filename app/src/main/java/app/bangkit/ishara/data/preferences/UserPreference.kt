package app.bangkit.ishara.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_LOGIN_STATUS_KEY = booleanPreferencesKey("user_login_status")

    fun getUserLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[USER_LOGIN_STATUS_KEY] ?: false
        }
    }

    suspend fun saveUserLoginStatus(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_LOGIN_STATUS_KEY] = isLogin
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}