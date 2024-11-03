package com.example.togetherpet.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferencesName = "token"
    private val sharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val tokenKey = "token";

    fun saveToken(token: String) {
        editor.putString(tokenKey, token)
        editor.apply()
    }

    fun getTokenOrThrow(): String {
        return requireNotNull(sharedPreferences.getString(tokenKey, null)) {
            "토큰이 존재하지 않습니다."
        }
    }

    fun hasToken(): Boolean{
        return getTokenOrThrow() != "토큰이 존재하지 않습니다."
    }
}