package com.example.togetherpet.data.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ReportService {

    @Multipart
    @POST("/api/v1/report")
    suspend fun registerReportByMissing(
        @Header("Authorization") token: String,
        @Part("reportCreateRequestDTO") reportCreateRequestDTO: RequestBody,
        @Part("files") files: List<MultipartBody.Part>
    ): Response<Unit>
}