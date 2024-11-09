package com.example.togetherpet.searching.report.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.entity.MissingEntity
import com.example.togetherpet.data.repository.MissingRepository
import com.example.togetherpet.data.repository.ReportRepository
import com.example.togetherpet.searching.searchingHome.ButtonType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDataViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val missingRepository: MissingRepository
) : ViewModel() {

    private val _selectedButton = MutableStateFlow(ButtonType.MISSING)
    val selectedButton: StateFlow<ButtonType> get() = _selectedButton

    private val _isMissingDataLoaded = MutableStateFlow(false)
    val isMissingDataLoaded: StateFlow<Boolean> = _isMissingDataLoaded

    val missingReports: StateFlow<List<MissingEntity>> = missingRepository.getAllMissingReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            missingReports.collectLatest { reports ->
                _isMissingDataLoaded.value = reports.isNotEmpty()
            }
        }
    }

    fun updateSelectedBtn(buttonType: ButtonType) {
        _selectedButton.value = buttonType
    }

    //근처 실종 의심 정보 가져 오기
    fun fetchSuspectedReports(latitude: Double, longitude: Double) {
        Log.d("child", "Reported Data Fetch")
        viewModelScope.launch {
            reportRepository.getReportByLocation(latitude, longitude)
        }
    }

    //근처 실종 정보 가져 오기
    fun fetchMissingReports(latitude: Double, longitude: Double) {
        Log.d("child", "Missing Data Fetch")
        viewModelScope.launch {
            missingRepository.getMissingNearBy(latitude, longitude)
        }
    }

    //받은 제보 정보 가져 오기
    fun fetchMyPetReports() {
        Log.d("child", "MyPet Data Fetch")
        viewModelScope.launch {
            reportRepository.getReportOwnByUser()
        }
    }
}