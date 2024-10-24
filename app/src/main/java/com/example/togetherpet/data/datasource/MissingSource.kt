package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.MissingDetailResponseDTO
import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import com.example.togetherpet.data.dto.MissingResponseDTO
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
    ): List<MissingResponseDTO> {
        val response = missingService.getMissingNearBy(latitude, longitude)

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

    suspend fun getMissingDetail(
        missingId: Number
    ): MissingDetailResponseDTO {
        val response = missingService.getMissingById(missingId)

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