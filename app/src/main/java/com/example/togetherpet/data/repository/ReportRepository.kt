package com.example.togetherpet.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.togetherpet.data.database.ReportDataBase
import com.example.togetherpet.data.datasource.ReportSource
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.data.entity.ReportEntity
import com.example.togetherpet.di.TypeConverterModule
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reportSource: ReportSource,
    private val tokenRepository: TokenRepository
) {
    private val db: ReportDataBase = Room.databaseBuilder(
        context.applicationContext,
        ReportDataBase::class.java,
        "report_database"
    ).addTypeConverter(TypeConverterModule(Gson()))
        .build()

    private val reportDao = db.reportDao()

    suspend fun registerReportByMissing(
        color: String,
        foundLatitude: Double,
        foundLongitude: Double,
        foundDate: String,  //변경(11.05)
        description: String,
        breed: String,
        gender: String,
        missingId: Long,
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
        foundDate: String,  //변경(11.05)
        description: String,
        breed: String,
        gender: String,
        files: List<File>
    ) {
        Log.d("yeong", "repo")

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
        reportDao.insertReports(
            reportSource.getRegisterOwnByUser(tokenRepository.getTokenOrThrow())
                .map { report ->
                    ReportEntity(
                        report.id,
                        report.latitude,
                        report.longitude,
                        mutableListOf(report.imageUrl),
                        null,
                        null,
                        null
                    )
                }
        )
    }

    suspend fun getReportByLocation(
        latitude: Double,
        longitude: Double,
    ) {
        reportDao.insertReports(
            reportSource.getReportByLocation(latitude, longitude)
                .map { report ->
                    ReportEntity(
                        report.id,
                        report.latitude,
                        report.longitude,
                        mutableListOf(report.imageUrl),
                        null,
                        null,
                        null
                    )
                }
        )
    }

    suspend fun getReportDetail(
        reportId: Long
    ) {
        val findReport = reportDao.getReportById(reportId)

        if (findReport != null) {
            val detailReport = reportSource.getReportDetail(reportId)
            findReport.imageUrl.addAll(detailReport.imageUrl)
            reportDao.updateReport(
                findReport.copy(
                    description = detailReport.description,
                    reporterName = detailReport.reporterName,
                    foundDate = detailReport.foundDate
                )
            )
        }

        // TODO Error 발생 로직 추가
    }
}