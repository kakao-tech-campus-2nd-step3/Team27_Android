package com.example.togetherpet.data.service

import com.example.togetherpet.data.dto.RegisterResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ReportService {

    @Multipart
    @POST("/api/v1/report")
    suspend fun registerReportByMissing(
        @Header("Authorization") token: String,
        @Part("reportCreateRequestDTO") reportCreateRequestDTO: RequestBody,
        @Part("files") files: List<MultipartBody.Part>
    ): Response<Unit>

    @GET("/api/v1/report/user")
    suspend fun getRegisterOwnByUser(
        @Header("Authorization") token: String
    ): Response<List<RegisterResponseDTO>>
}