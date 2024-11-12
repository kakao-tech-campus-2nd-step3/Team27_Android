package com.example.togetherpet.searching.report.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.repository.ReportRepository
import com.example.togetherpet.searching.report.ReportStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportMissingViewModel @Inject constructor(
    private val reportRepository : ReportRepository
) : ViewModel() {
    private val _reportStatus = MutableStateFlow(ReportStatus.IDLE)
    val reportStatus: StateFlow<ReportStatus> = _reportStatus

    fun reportMissingObserve(
        color: String,
        gender: String,
        breed: String,
        description: String,
        foundDate: String,
        foundLatitude: Double,
        foundLongitude: Double,
        missingId : Long,
        file: List<File>
    ){
        val parsedDate = convertDateFormat(foundDate)

        //HTTP 통신
        viewModelScope.launch {
            try {
                reportRepository.registerReportByMissing(color, foundLatitude, foundLongitude, parsedDate, description, breed, gender, missingId, file)
                _reportStatus.value = ReportStatus.SUCCESS // 성공 시
            } catch (e: Exception) {
                _reportStatus.value = ReportStatus.ERROR // 실패 시
            } finally {
                // 초기화 또는 다음 요청을 위해 IDLE 상태로 되돌림
                _reportStatus.value = ReportStatus.IDLE
            }
        }
    }

    private fun convertDateFormat(foundData: String): String {
        // 입력
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm", Locale.KOREA)
        // 출력
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // LocalDateTime 객체로 변환 후 다시 문자열로 변환
        val dateTime = LocalDateTime.parse(foundData, inputFormatter)
        return dateTime.format(outputFormatter)
    }
}