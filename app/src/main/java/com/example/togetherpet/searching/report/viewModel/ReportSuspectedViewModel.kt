package com.example.togetherpet.searching.report.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ReportSuspectedViewModel @Inject constructor(
    private val reportRepository : ReportRepository
) : ViewModel() {
    fun reportSuspected(
        color: String,
        gender: String,
        breed: String,
        description: String,
        foundDate: String,
        foundLatitude: Double,
        foundLongitude: Double,
        file: List<File>
    ){
        //foundDate type : string -> LocalDateTime
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm", Locale.getDefault())
        val parsedDate = LocalDateTime.parse(foundDate, formatter)

        //HTTP 통신
        viewModelScope.launch {
            reportRepository.registerReportWithoutMissing(
                color, foundLatitude, foundLongitude, parsedDate, description, breed, gender, file
            )
        }
    }
}
