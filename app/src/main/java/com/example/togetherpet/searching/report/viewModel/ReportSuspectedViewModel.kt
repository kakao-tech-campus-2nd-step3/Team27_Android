package com.example.togetherpet.searching.report.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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
        foundDate: LocalDateTime,
        foundLatitude: Double,
        foundLongitude: Double,
        uri: String
    ){
        // uri -> 절대 경로 로직 작성할 곳

        //HTTP 통신
        viewModelScope.launch {
            /*reportRepository.registerReportWithoutMissing(

            )*/
        }
    }
}