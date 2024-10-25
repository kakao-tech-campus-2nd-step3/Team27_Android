package com.example.togetherpet

import android.util.Log
import javax.inject.Inject

// todo : 수정 필요
class TokenRepositoryImpl @Inject constructor(private val tokenDataSource: TokenDataSource) :
    TokenRepository {
    override suspend fun postToken(token: String): Boolean {
        Log.d("testt", "token : ${token}, repo")
        val response = tokenDataSource.postToken(token)
        return response.existToken
    }
}