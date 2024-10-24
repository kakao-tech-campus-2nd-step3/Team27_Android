package com.example.togetherpet.data.repository

import com.example.togetherpet.data.datasource.MissingSource
import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import javax.inject.Inject

class MissingRepository @Inject constructor(
    private val missingSource: MissingSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun registerMissing(
        missingRegisterRequestDTO: MissingRegisterRequestDTO
    ) {
        missingSource.registerMissing(
            tokenRepository.getTokenOrThrow(),
            missingRegisterRequestDTO
        )
    }

    suspend fun getMissingNearBy(
        latitude: Double,
        longitude: Double
    ) {
        missingSource.getMissingNearBy(latitude, longitude)
    }

    suspend fun getMissingByMissingId(
        missingId: Number
    ) {
        missingSource.getMissingByMissingId(missingId)
    }
}