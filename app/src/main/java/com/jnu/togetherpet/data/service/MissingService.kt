package com.jnu.togetherpet.data.service

import com.jnu.togetherpet.data.dto.MissingDetailResponseDTO
import com.jnu.togetherpet.data.dto.MissingRegisterRequestDTO
import com.jnu.togetherpet.data.dto.MissingResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface MissingService {

    @POST("/api/v1/missing")
    suspend fun registerMissing(
        @Header("Authorization") token: String,
        @Body missingRegisterRequestDTO: MissingRegisterRequestDTO
    ): Response<Unit>

    @GET("/api/v1/missing")
    suspend fun getMissingNearBy(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<List<MissingResponseDTO>>

    @GET("/api/v1/missing/{missing-id}")
    suspend fun getMissingByMissingId(
        @Path("missing-id") missingId: Number
    ): Response<MissingDetailResponseDTO>
}