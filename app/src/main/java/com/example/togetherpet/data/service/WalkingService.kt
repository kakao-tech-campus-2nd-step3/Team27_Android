package com.example.togetherpet.data.service

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WalkingService {
    @POST("/api/v1/walk")
    suspend fun postWalk(
        @Header("Authorization")token : String,
        @Body("WalkRequestDTO")

    )

}