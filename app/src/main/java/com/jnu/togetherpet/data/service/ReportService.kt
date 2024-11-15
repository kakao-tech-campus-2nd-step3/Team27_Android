package com.jnu.togetherpet.data.service

import com.jnu.togetherpet.data.dto.ReportDetailResponseDTO
import com.jnu.togetherpet.data.dto.ReportResponseDTO
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
        @Part vararg files: MultipartBody.Part
    ): Response<Unit>

    @GET("/api/v1/report/user")
    suspend fun getReportOwnByUser(
        @Header("Authorization") token: String
    ): Response<List<ReportResponseDTO>>

    @GET("/api/v1/report/location")
    suspend fun getReportByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<List<ReportResponseDTO>>

    @GET("/api/v1/report/{report-id}")
    suspend fun getReportDetail(
        @Path("report-id") reportId: Number
    ): Response<ReportDetailResponseDTO>
}