package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.WalkingRequestDTO
import com.example.togetherpet.data.dto.WalkingResponseDTO
import com.example.togetherpet.data.service.WalkingService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkingNetworkSource @Inject constructor(
    private val walkingService: WalkingService,
    private val gson: Gson
) {
    suspend fun postWalkingData(
        token: String,
        walkingRequestDTO: WalkingRequestDTO
    ) {
        val response = walkingService.postWalkingData(
            token, walkingRequestDTO
        )

        if (!response.isSuccessful) throw APIException(
            gson.fromJson(
                response.errorBody()?.string(),
                ErrorResponse::class.java
            )
        )
    }

    suspend fun getWalkingDataWithDate(token: String, date: String): List<WalkingResponseDTO>? {
        val response = walkingService.getWalkingDataWithDate(
            token, date
        )

        if (response.isSuccessful) {
            return response.body()
        } else if (!response.isSuccessful) {
            throw APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
        }
        return null
    }
}