package com.example.togetherpet.searching.report.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.data.repository.KakaoLocalRepository
import com.example.togetherpet.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        foundDate: String,
        foundLatitude: Double,
        foundLongitude: Double,
        uri: String
    ){
        //found Date string -> LocalDateTime 로직 작성할 곳
        // uri -> 절대 경로 로직 작성할 곳

        //HTTP 통신
        viewModelScope.launch {
            /*reportRepository.registerReportWithoutMissing(

            )*/
        }
    }
}