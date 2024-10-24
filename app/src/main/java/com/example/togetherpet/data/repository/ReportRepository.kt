package com.example.togetherpet.data.repository

import com.example.togetherpet.data.datasource.ReportSource
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val reportSource: ReportSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun registerReportByMissing(
        color: String,
        foundLatitude: Double,
        foundLongitude: Double,
        foundDate: LocalDateTime,
        description: String,
        breed: String,
        gender: String,
        missingId: Number,
        files: List<File>
    ) {
        reportSource.registerReport(
            tokenRepository.getTokenOrThrow(),
            ReportCreateRequestDTO(
                color,
                foundLatitude,
                foundLongitude,
                foundDate,
                description,
                breed,
                gender,
                missingId
            ),
            files
        )
    }

    suspend fun registerReportWithoutMissing(
        color: String,
        foundLatitude: Double,
        foundLongitude: Double,
        foundDate: LocalDateTime,
        description: String,
        breed: String,
        gender: String,
        files: List<File>
    ) {
        reportSource.registerReport(
            tokenRepository.getTokenOrThrow(),
            ReportCreateRequestDTO(
                color,
                foundLatitude,
                foundLongitude,
                foundDate,
                description,
                breed,
                gender,
                null
            ),
            files
        )
    }

    suspend fun getReportOwnByUser() {
        reportSource.getRegisterOwnByUser(tokenRepository.getTokenOrThrow())
    }

    suspend fun getReportDetail(
        reportId: Number
    ) {
        reportSource.getReportDetail(reportId)
    }
}