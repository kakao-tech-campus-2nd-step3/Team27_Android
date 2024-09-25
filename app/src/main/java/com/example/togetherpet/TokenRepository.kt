package com.example.togetherpet

interface TokenRepository {
    // todo : 수정 필요
    suspend fun postToken(token: String) : Boolean
}