package com.example.togetherpet.fragment

import android.os.SystemClock
import com.example.togetherpet.testData.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkingRecordRepository @Inject constructor(){
    fun getRecordOfDate(date : LocalDate) : ArrayList<WalkingRecord>{
        return arrayListOf<WalkingRecord>(WalkingRecord(10,System.currentTimeMillis(),3600000,
            SystemClock.elapsedRealtime(), 10, arrayListOf(LatLng.from(34.7505, 127.6719), LatLng.from(34.765, 127.6719), LatLng.from(34.765, 127.6819), LatLng.from(34.7505, 127.6819))), WalkingRecord(10,System.currentTimeMillis(),3600000,
            SystemClock.elapsedRealtime(), 10, arrayListOf(LatLng.from(34.7505, 127.6719), LatLng.from(34.765, 127.6719), LatLng.from(34.765, 127.6819), LatLng.from(34.7505, 127.6819))), WalkingRecord(10,System.currentTimeMillis(),3600000,
            SystemClock.elapsedRealtime(), 10, arrayListOf(LatLng.from(34.7505, 127.6719), LatLng.from(34.765, 127.6719), LatLng.from(34.765, 127.6819), LatLng.from(34.7505, 127.6819))))

    }
}