package com.example.togetherpet.data.service

import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MissingService {

    @POST("/api/v1/missing")
    suspend fun registerMissing(
        @Header("Authorization") token: String,
        @Body missingRegisterRequestDTO: MissingRegisterRequestDTO
    ): Response<Unit>
}