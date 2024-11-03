package com.example.togetherpet.fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.togetherpet.testData.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WalkingPetRecordViewModel @Inject constructor(private val walkingRecordRepository: WalkingRecordRepository) :
    ViewModel() {
    private val _allDistance = MutableStateFlow<Long>(0)
    private val _allTime = MutableStateFlow<Long>(0)
    private val _arrayRecord = MutableStateFlow<ArrayList<WalkingRecord>>(ArrayList())
    private val _selectDay = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _base = MutableStateFlow<Long>(0)
    private val _distance = MutableStateFlow<Long>(0)
    private val _calories = MutableStateFlow<Long>(0)
    private val _time = MutableStateFlow<Long>(0)
    private val _arrayLoc = MutableStateFlow<ArrayList<LatLng>>(ArrayList())

    val distance: StateFlow<Long> get() = _distance.asStateFlow()
    val calories: StateFlow<Long> get() = _calories.asStateFlow()
    val time: StateFlow<Long> get() = _time.asStateFlow()
    val base : StateFlow<Long> get() = _base.asStateFlow()
    val arrayLoc: StateFlow<ArrayList<LatLng>> get() = _arrayLoc.asStateFlow()


    val allDistance: StateFlow<Long> get() = _allDistance.asStateFlow()
    val allTime: StateFlow<Long> get() = _allTime.asStateFlow()
    val arrayRecord: StateFlow<ArrayList<WalkingRecord>> get() = _arrayRecord.asStateFlow()
    val selectDay: StateFlow<LocalDate> get() = _selectDay.asStateFlow()

    fun getRecord(date: LocalDate) {
        _selectDay.value = date
        _arrayRecord.value = walkingRecordRepository.getRecordOfDate(date)
        calculateAllDistance()
        calculateAllTime()
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
        _base.value = selectedDetail.baseTime
    }

}