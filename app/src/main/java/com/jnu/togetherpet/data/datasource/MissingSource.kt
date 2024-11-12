package com.jnu.togetherpet.data.datasource

import android.util.Log
import com.jnu.togetherpet.data.dto.MissingDetailResponseDTO
import com.jnu.togetherpet.data.dto.MissingRegisterRequestDTO
import com.jnu.togetherpet.data.dto.MissingResponseDTO
import com.jnu.togetherpet.data.service.MissingService
import com.jnu.togetherpet.exception.APIException
import com.jnu.togetherpet.exception.ErrorResponse
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

    suspend fun getMissingByMissingId(
        missingId: Number
    ): MissingDetailResponseDTO {
        val response = missingService.getMissingByMissingId(missingId)
        Log.d("MissingSource", "API call response: $response")

        if (response.isSuccessful) {
            val responseBody = response.body()
            Log.d("MissingSource", "API call successful, response body: $responseBody")
            return response.body()!!
        }

        Log.e("MissingSource", "API call failed, error: ${response.errorBody()?.string()}")
        throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }
}