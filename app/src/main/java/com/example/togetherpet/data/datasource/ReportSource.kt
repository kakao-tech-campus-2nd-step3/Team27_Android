package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.ReportCreateRequestDTO
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

        if (!response.isSuccessful) {
            throw APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
        }
    }

    suspend fun getRegisterOwnByUser(
        token: String
    ) {
        val response = reportService.getRegisterOwnByUser(token)

        if (response.isSuccessful) {
            // TODO 저장 로직
        }

        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }
}