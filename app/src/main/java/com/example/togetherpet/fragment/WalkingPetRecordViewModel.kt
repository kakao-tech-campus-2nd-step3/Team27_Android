package com.example.togetherpet.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.repository.WalkingRepository
import com.example.togetherpet.testData.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class WalkingPetRecordViewModel @Inject constructor(private val walkingRepository: WalkingRepository) :
    ViewModel() {
    private val _allDistance = MutableStateFlow<Long>(0)
    private val _allTime = MutableStateFlow<Long>(0)
    private val _arrayRecord = MutableStateFlow<ArrayList<WalkingRecord>>(ArrayList())
    private val _selectDay = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _base = MutableStateFlow<Long>(0)
    private val _distance = MutableStateFlow<Long>(0)
    private val _calories = MutableStateFlow<Long>(0)
    private val _time = MutableStateFlow<Long>(0)
    private val _startTime = MutableStateFlow<LocalDateTime>(LocalDateTime.now())
    private val _endTime = MutableStateFlow<LocalDateTime>(LocalDateTime.now())
    private val _arrayLoc = MutableStateFlow<ArrayList<LatLng>>(ArrayList())

    val distance: StateFlow<Long> get() = _distance.asStateFlow()
    val calories: StateFlow<Long> get() = _calories.asStateFlow()
    val time: StateFlow<Long> get() = _time.asStateFlow()
    val base : StateFlow<Long> get() = _base.asStateFlow()
    val arrayLoc: StateFlow<ArrayList<LatLng>> get() = _arrayLoc.asStateFlow()
    val allDistance: StateFlow<Long> get() = _allDistance.asStateFlow()
    val allTime: StateFlow<Long> get() = _allTime.asStateFlow()
    val arrayRecord: StateFlow<ArrayList<WalkingRecord>> get() = _arrayRecord.asStateFlow()
    val startTime:StateFlow<LocalDateTime> get() = _startTime.asStateFlow()
    val endTime : StateFlow<LocalDateTime> get() = _endTime.asStateFlow()
    val selectDay: StateFlow<LocalDate> get() = _selectDay.asStateFlow()

    private var walkCount: Int = 0

    private val _walkingData = MutableStateFlow(WalkingData(0, 0, 0))
    val walkingData: StateFlow<WalkingData> get() = _walkingData.asStateFlow()
    
    fun getRecord(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectDay.value = date
            _arrayRecord.value = walkingRepository.getWalkingDataWithDateFromServer(date)
            Log.d("testt", "${_arrayRecord.value}")
            calculateAllDistance()
            calculateAllTime()
        }
    }

    fun calculateAllDistance() {
        var sumDistance = 0L
        _arrayRecord.value.forEach {
            sumDistance += it.distance
        }
        _allDistance.value = sumDistance
    }

    fun calculateAllTime() {
        var sumTime = 0L
        _arrayRecord.value.forEach {
            sumTime += it.time
        }
        _allTime.value = sumTime
    }

    fun getSelectedDetailRecord(position : Int){
        val selectedDetail = _arrayRecord.value[position]
        _distance.value = selectedDetail.distance
        _time.value = selectedDetail.time
        _calories.value = selectedDetail.calories
        _arrayLoc.value = selectedDetail.route
        _startTime.value = selectedDetail.startTime
        _endTime.value = selectedDetail.endTime
    }
    fun getWalkingData(){
        updateTodayWalkCount()
        _walkingData.value = WalkingData(
            distance = _allDistance.value,
            time = _allTime.value,
            todayWalkCount = walkCount
        )
    }

    //오늘 산책 횟수
    private fun updateTodayWalkCount() {
        val today = LocalDate.now()
        val count = _arrayRecord.value.count {
            val recordDate = it.date
            recordDate == today
        }
        walkCount = count
    }
}

data class WalkingData(
    val distance: Long,
    val time: Long,
    val todayWalkCount: Int
)
