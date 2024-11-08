package com.example.togetherpet.searching.report.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.entity.ReportEntity
import com.example.togetherpet.data.repository.MissingRepository
import com.example.togetherpet.data.repository.ReportRepository
import com.example.togetherpet.searching.report.ReportStatus
import com.example.togetherpet.searching.searchingHome.ButtonType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDataViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val missingRepository: MissingRepository
) : ViewModel() {

    private val _selectedButton = MutableStateFlow<ButtonType>(ButtonType.MISSING)
    val selectedButton: StateFlow<ButtonType> get() = _selectedButton

    fun updateSelectedBtn(buttonType: ButtonType){
        _selectedButton.value = buttonType
    }

    //근처 실종 의심 정보 가져 오기
    suspend fun fetchSuspectedReports(latitude: Double, longitude: Double) {
        Log.d("yeong", "SuspectedData Fetch")
        //목격 제보 데이터 report db 저장
        reportRepository.getReportByLocation(latitude, longitude)
    }

    //근처 실종 정보 가져 오기
    suspend fun fetchMissingReports(latitude: Double, longitude: Double){
        Log.d("yeong", "MissingData Fetch")
        missingRepository.getMissingNearBy(latitude,longitude)
    }

    //받은 제보 정보 가져 오기
    suspend fun fetchMyPetReports(){
        Log.d("yeong", "MyPetData Fetch")
        reportRepository.getReportOwnByUser()
    }
}