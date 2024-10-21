package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import com.example.togetherpet.data.service.MissingService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MissingSource @Inject constructor(
    private val missingService: MissingService,
    private val gson: Gson
) {
    suspend fun registerMissing(
        token: String,
        missingRegisterRequestDTO: MissingRegisterRequestDTO
    ) {
        val response = missingService.registerMissing(token, missingRegisterRequestDTO)

        if (!response.isSuccessful) {
            throw APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
        }
    }

    suspend fun getMissingNearBy(
        latitude: Double,
        longitude: Double
    ) {
        val response = missingService.getMissingNearBy(latitude, longitude)

        if (response.isSuccessful) {
            // TODO 정상 응답 로직
        }

        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }
}