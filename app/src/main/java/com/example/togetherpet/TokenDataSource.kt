package com.example.togetherpet

interface TokenDataSource {

    // todo : response 수정 필요
    suspend fun postToken(
        token: String
    ) : LoginResponseDto
}