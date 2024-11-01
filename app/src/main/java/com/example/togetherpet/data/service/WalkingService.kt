package com.example.togetherpet.data.service

import com.example.togetherpet.data.dto.WalkingRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WalkingService {
    @POST("/api/v1/walk")
    suspend fun postWalkingData(
        @Header("Authorization")token : String,
        @Body walkingRequestDTO : WalkingRequestDTO
    ) : Response<Unit>
}