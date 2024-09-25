package com.example.togetherpet

import javax.inject.Inject

// todo : 수정 필요
class TokenRepositoryImpl @Inject constructor(private val tokenDataSource: TokenDataSource) :
    TokenRepository {
    override suspend fun postToken(token: String): Boolean {
        val response = tokenDataSource.postToken(token)
        return response.existToken
    }
}