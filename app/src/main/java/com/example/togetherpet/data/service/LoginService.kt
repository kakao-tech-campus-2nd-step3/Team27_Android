package com.example.togetherpet.data.service

import com.example.togetherpet.data.dto.LoginRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/v1/login")
    suspend fun login(
        @Body body: LoginRequestDTO
    ): Response<Unit>

}