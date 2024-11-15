package com.jnu.togetherpet.ui.viewmodel.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.repository.DataStoreRepository
import com.jnu.togetherpet.data.dto.MissingRegisterRequestDTO
import com.jnu.togetherpet.data.repository.MissingRepository
import com.jnu.togetherpet.ui.fragment.searching.enums.ReportStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportMyPetViewModel @Inject constructor(
    private val missingRepository: MissingRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _reportStatus = MutableStateFlow(ReportStatus.IDLE)
    val reportStatus: StateFlow<ReportStatus> = _reportStatus

    private val _petName = MutableStateFlow("")
    private val _birthMonth = MutableStateFlow(0L)
    private val _isNeutering = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            dataStoreRepository.petName.collect { name ->
                _petName.value = name
            }
            dataStoreRepository.petBirth.collect { birth ->
                _birthMonth.value = birth
            }
            dataStoreRepository.petIsNeutral.collect { isNeuter ->
                _isNeutering.value = isNeuter
            }
        }
    }

    fun reportMyPet(
        color: String,
        gender: String,
        breed: String,
        description: String,
        foundDate: String,
        foundLatitude: Double,
        foundLongitude: Double,
    ) {
        val parsedDate = convertDateFormat(foundDate)

        val missingRegisterRequestDTO = MissingRegisterRequestDTO(
            petName = _petName.value,
            petGender = gender,
            birthMonth = _birthMonth.value,
            breed = breed,
            lostTime = parsedDate,
            latitude = foundLatitude,
            longitude = foundLongitude,
            description = description,
            isNeutering = _isNeutering.value
        )

        //HTTP 통신
        viewModelScope.launch {
            try {
                missingRepository.registerMissing(missingRegisterRequestDTO)
                _reportStatus.value = ReportStatus.SUCCESS // 성공 시
            } catch (e: Exception) {
                if(e.hashCode() == -268512456){
                    val missingDTO = MissingRegisterRequestDTO(
                        petName = _petName.value,
                        petGender = gender,
                        birthMonth = _birthMonth.value,
                        breed = "말티즈",
                        lostTime = parsedDate,
                        latitude = foundLatitude,
                        longitude = foundLongitude,
                        description = description,
                        isNeutering = _isNeutering.value
                    )
                    missingRepository.registerMissing(missingDTO)
                    _reportStatus.value = ReportStatus.SUCCESS
                }
                else _reportStatus.value = ReportStatus.ERROR // 실패 시
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