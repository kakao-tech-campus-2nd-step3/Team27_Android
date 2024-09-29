package com.example.togetherpet

import android.util.Log
import com.example.togetherpet.Login.LoginResponseDto
import com.example.togetherpet.Login.LoginService
import javax.inject.Inject


class TokenDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : TokenDataSource {
    // todo : 수정 필요
    override suspend fun postToken(token: String): LoginResponseDto {
        Log.d("testt", "token : ${token}")
        return loginService.postLoginToken(token)
    }
}