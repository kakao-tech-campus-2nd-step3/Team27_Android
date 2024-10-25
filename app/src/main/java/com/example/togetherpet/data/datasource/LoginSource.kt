package com.example.togetherpet.data.datasource

import android.util.Log
import com.example.togetherpet.data.dto.LoginRequestDTO
import com.example.togetherpet.data.service.LoginService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSource @Inject constructor(
    private val loginService: LoginService,
    private val gson: Gson
) {
    suspend fun login(email: String): String {
        val response = loginService.login(LoginRequestDTO(email))

        if (response.isSuccessful) {
            Log.e("Token", response.headers()["Authorization"].toString())
            return response.headers()["Authorization"].toString()
        }

        throw APIException(gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
    }
}