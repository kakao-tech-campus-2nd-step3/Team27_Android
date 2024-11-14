package com.jnu.togetherpet.data.datasource

import android.util.Log
import com.jnu.togetherpet.data.dto.WalkingRequestDTO
import com.jnu.togetherpet.data.dto.WalkingResponseDTO
import com.jnu.togetherpet.data.service.WalkingService
import com.jnu.togetherpet.exception.APIException
import com.jnu.togetherpet.exception.ErrorResponse
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

        Log.d("testt", "${response.errorBody()}, ${response.body()}, ${response.code()}")

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
        Log.d("testt", "${response.body()}, ${response.code()}, ${response.errorBody()?.string()}")
        return if (response.isSuccessful) {
            response.body()
        } else  {
            throw APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
        }
    }
}