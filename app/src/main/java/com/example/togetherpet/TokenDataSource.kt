package com.example.togetherpet

import com.example.togetherpet.Login.LoginResponseDto

interface TokenDataSource {

    // todo : response 수정 필요
    suspend fun postToken(
        token: String
    ) : LoginResponseDto
}