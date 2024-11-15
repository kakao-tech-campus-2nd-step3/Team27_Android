package com.jnu.togetherpet.ui.viewmodel.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.entity.MissingEntity
import com.jnu.togetherpet.data.entity.ReportEntity
import com.jnu.togetherpet.data.repository.MissingRepository
import com.jnu.togetherpet.data.repository.ReportRepository
import com.jnu.togetherpet.ui.fragment.searching.enums.ButtonType
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val missingReports: StateFlow<List<MissingEntity>> = missingRepository.getAllMissingReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 내 반려동물 제보 StateFlow
    val myPetReports: StateFlow<List<ReportEntity>> = reportRepository.getOwnReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 근처 목격 제보 StateFlow
    val nearbySuspectedReports: StateFlow<List<ReportEntity>> = reportRepository.getNearbyReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _missingDetail = MutableStateFlow<MissingEntity?>(null)
    val missingDetail: StateFlow<MissingEntity?> = _missingDetail

    private val _suspectedDetail = MutableStateFlow<ReportEntity?>(null)
    val suspectedDetail: StateFlow<ReportEntity?> = _suspectedDetail

    private val _centerPos = MutableStateFlow<LatLng>(LatLng.from(0.0, 0.0))
    val centerPos get() = _centerPos.asStateFlow()

    fun updateSelectedBtn(buttonType: ButtonType) {
        _selectedButton.value = buttonType
    }

    //근처 실종 의심 정보 가져 오기
    fun fetchSuspectedReports(latitude: Double, longitude: Double) {
        Log.d("child", "Reported Data Fetch")
        viewModelScope.launch(Dispatchers.IO) {
            reportRepository.getReportByLocation(latitude,longitude)
        }
    }

    //근처 실종 의심 자세한 정보 가져 오기
    fun fetchSuspectedDetails(reportId:Long) {
        if (_suspectedDetail.value != null) {
            Log.d("child", "이미 데이터가 로드되어 있음")
            return
        }
        Log.d("child", "Reported Data Fetch - Report ID: $reportId")
        viewModelScope.launch {
            Log.d("child", "viewModelScope안에 들어옴 - Report ID: $reportId")
            val detail = reportRepository.getReportDetail(reportId)
            _suspectedDetail.value = detail
            Log.d("child", "viewModelScope에서 이제 나감")
        }
    }

    //근처 실종 정보 가져 오기
    fun fetchMissingReports(latitude: Double, longitude: Double) {
        Log.d("child", "Missing Data Fetch")
        viewModelScope.launch(Dispatchers.IO) {
            missingRepository.getMissingNearBy(latitude, longitude)
        }
    }

    fun setCenterPos(latitude: Double, longitude: Double){
        _centerPos.value = LatLng.from(latitude, longitude)
    }
    //근처 실종 정보 자세한 정보 가져 오기
    fun fetchMissingDetails(missingId:Long){
        Log.d("child","Missing Detail Fetch")
        viewModelScope.launch {
            val detail = missingRepository.getMissingByMissingId(missingId)
            _missingDetail.value = detail
            Log.d("ReportDataViewModel", "Fetched detail: $detail")
        }
    }

    //받은 제보 정보 가져 오기
    fun fetchMyPetReports() {
        Log.d("child", "MyPet Data Fetch")
        viewModelScope.launch(Dispatchers.IO) {
            reportRepository.getReportOwnByUser()
        }
    }

    fun clearSuspectedDetail() {
        _suspectedDetail.value = null
    }
}