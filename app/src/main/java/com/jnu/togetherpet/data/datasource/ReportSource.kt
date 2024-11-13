package com.jnu.togetherpet.data.datasource

import android.util.Log
import com.jnu.togetherpet.data.dto.ReportCreateRequestDTO
import com.jnu.togetherpet.data.dto.ReportDetailResponseDTO
import com.jnu.togetherpet.data.dto.ReportResponseDTO
import com.jnu.togetherpet.data.service.ReportService
import com.jnu.togetherpet.exception.APIException
import com.jnu.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
        Log.d("sendReport", "File List in ReportSource: $files")
        val fileParts = files.map { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "files",
                file.name,
                requestBody
            )

            Log.d("sendReport", "Multipart Part - File Name: ${file.name}, File Size: ${file.length()} bytes")

            part
        }


        val response = reportService.registerReportByMissing(
            token,
            gson.toJson(reportCreateRequestDTO)
                .toRequestBody("application/json".toMediaTypeOrNull()),
            *fileParts.toTypedArray() // 변환한 파트를 배열로 전달
        )


        if (!response.isSuccessful) {
            val e = APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
            throw e
        }
    }

    suspend fun getRegisterOwnByUser(
        token: String
    ): List<ReportResponseDTO> {
        Log.d("sendReport","getRegisterOwnByUser()")
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
        Log.d("sendReport","getReportByLocation()")
        val response = reportService.getReportByLocation(latitude, longitude)
        Log.d("MissingSource", "JSON Response: $response")

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