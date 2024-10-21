package com.example.togetherpet.data.repository

import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import com.example.togetherpet.data.service.MissingService
import javax.inject.Inject

class MissingRepository @Inject constructor(
    private val missingService: MissingService
) {
    suspend fun registerMissing(
        token: String,
        missingRegisterRequestDTO: MissingRegisterRequestDTO
    ) {
        missingService.registerMissing(token, missingRegisterRequestDTO)
    }

    suspend fun getMissingNearBy(
        latitude: Double,
        longitude: Double
    ) {
        missingService.getMissingNearBy(latitude, longitude)
    }

    suspend fun getMissingByMissingId(
        missingId: Number
    ) {
        missingService.getMissingById(missingId)
    }
}