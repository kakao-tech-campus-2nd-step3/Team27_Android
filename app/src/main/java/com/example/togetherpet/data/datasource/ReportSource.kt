package com.example.togetherpet.data.datasource

import android.util.Log
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.data.dto.ReportDetailResponseDTO
import com.example.togetherpet.data.dto.ReportResponseDTO
import com.example.togetherpet.data.service.ReportService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportSource @Inject constructor(
    private val reportService: ReportService,
    private val gson: Gson
) {
    suspend fun registerReport(
        token: String,
        reportCreateRequestDTO: ReportCreateRequestDTO,
        files: List<File>
    ) {
        Log.d("yeong", "${files.last()}")

        try {
            val response = reportService.registerReportByMissing(
                token,
                gson.toJson(reportCreateRequestDTO)
                    .toRequestBody("application/json".toMediaTypeOrNull()),
                files.stream()
                    .map { file ->
                        MultipartBody.Part.createFormData(
                            "reportImage",
                            file.name,
                            file.asRequestBody("image/*".toMediaTypeOrNull())
                        )
                    }
                    .collect(Collectors.toList())
            )

            Log.d("yeong", "Response Success: ${response.isSuccessful}")
            Log.d("yeong", "${response.body()}, ${response.errorBody()}")

            if (!response.isSuccessful) {
                throw APIException(
                    gson.fromJson(
                        response.errorBody()?.string(),
                        ErrorResponse::class.java
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("yeong", "Exception occurred: ${e.message}")
        }
    }

    suspend fun getRegisterOwnByUser(
        token: String
    ): List<ReportResponseDTO> {
        val response = reportService.getReportOwnByUser(token)

        if (response.isSuccessful) {
            return response.body()!!
        }

        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }

    suspend fun getReportByLocation(
        latitude: Double,
        longitude: Double
    ): List<ReportResponseDTO> {
        val response = reportService.getReportByLocation(latitude, longitude)

        if (response.isSuccessful) {
            return response.body()!!
        }

        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }

    suspend fun getReportDetail(
        reportId: Number
    ): ReportDetailResponseDTO {
        val response = reportService.getReportDetail(reportId)

        if (response.isSuccessful) {
            return response.body()!!
        }

        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }
}