package com.example.togetherpet.data.repository

import com.example.togetherpet.data.datasource.ReportSource
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val reportSource: ReportSource
) {
    suspend fun registerReportByMissing(
        token: String,
        reportCreateRequestDTO: ReportCreateRequestDTO,
        files: List<File>
    ) {
        reportSource.registerReportByMissing(token, reportCreateRequestDTO, files)
    }
}