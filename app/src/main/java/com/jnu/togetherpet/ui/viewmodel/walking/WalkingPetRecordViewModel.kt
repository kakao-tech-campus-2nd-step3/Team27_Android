package com.jnu.togetherpet.ui.viewmodel.walking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.repository.UserRepository
import com.jnu.togetherpet.data.repository.WalkingRepository
import com.jnu.togetherpet.data.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class WalkingPetRecordViewModel @Inject constructor(
    private val walkingRepository: WalkingRepository,
    private val userRepository: UserRepository,
) :
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
    private val _petName = MutableStateFlow(" ")

    val distance: StateFlow<Long> get() = _distance.asStateFlow()
    val calories: StateFlow<Long> get() = _calories.asStateFlow()
    val time: StateFlow<Long> get() = _time.asStateFlow()
    val base: StateFlow<Long> get() = _base.asStateFlow()
    val arrayLoc: StateFlow<ArrayList<LatLng>> get() = _arrayLoc.asStateFlow()
    val allDistance: StateFlow<Long> get() = _allDistance.asStateFlow()
    val allTime: StateFlow<Long> get() = _allTime.asStateFlow()
    val arrayRecord: StateFlow<ArrayList<WalkingRecord>> get() = _arrayRecord.asStateFlow()
    val startTime: StateFlow<LocalDateTime> get() = _startTime.asStateFlow()
    val endTime: StateFlow<LocalDateTime> get() = _endTime.asStateFlow()
    val selectDay: StateFlow<LocalDate> get() = _selectDay.asStateFlow()
    val petName: StateFlow<String> get() = _petName.asStateFlow()


    private var walkCount: Int = 0

    private val _walkingData = MutableStateFlow(WalkingData(0, "0", 0))
    val walkingData: StateFlow<WalkingData> get() = _walkingData.asStateFlow()

    init {
        setPetName()
    }

    private fun setPetName() {
        viewModelScope.launch(Dispatchers.IO) {
            _petName.value = userRepository.getUserData().petName
        }
    }

    //// 배포용위해 GPS 기능을 통해 얻은 위치 정보를 데이터베이스에 저장하고 활용하도록 변경
    fun getRecord(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectDay.value = date
            val walkingRecordList = walkingRepository.getWalkingDataWithDateFromServer(date)
            _arrayRecord.value = walkingRecordList
//            Log.d("testt", "${_arrayRecord.value}")
            calculateAllDistance()
            calculateAllTime()
        }
    }

    fun getRecordToLocal(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectDay.value = date
            val walkingRecordList = walkingRepository.getWalkingDataWithDateFromLocal(date)
            _arrayRecord.value = walkingRecordList
            calculateAllDistance()
            calculateAllTime()
            getWalkingData()
            Log.d("testt", "getRecord : ${_allDistance.value} ${_allTime.value}")
        }
    }

    private fun calculateAllDistance() {
        var sumDistance = 0L
        _arrayRecord.value.forEach {
            sumDistance += it.distance
        }
        _allDistance.value = sumDistance
    }

    private fun calculateAllTime() {
        var sumTime = 0L
        _arrayRecord.value.forEach {
            sumTime += it.time
        }
        _allTime.value = sumTime
    }

    fun getSelectedDetailRecord(position: Int) {
        val selectedDetail = _arrayRecord.value[position]
        _distance.value = selectedDetail.distance
        _time.value = selectedDetail.time
        _calories.value = selectedDetail.calories
        _arrayLoc.value = selectedDetail.route
        _startTime.value = selectedDetail.startTime
        _endTime.value = selectedDetail.endTime
    }

    private fun getWalkingData() {
        updateTodayWalkCount()
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        _walkingData.value = WalkingData(
            distance = _allDistance.value,
            time = dateFormat.format(Date(_allTime.value)),
            todayWalkCount = walkCount
        )
        Log.d("testt", "walking : ${_walkingData.value}, ${_arrayRecord.value}")
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
    val time: String,
    val todayWalkCount: Int
)
