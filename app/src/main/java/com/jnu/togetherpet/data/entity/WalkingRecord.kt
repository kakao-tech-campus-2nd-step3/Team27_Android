package com.jnu.togetherpet.data.entity

import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.LocalDateTime

data class WalkingRecord(
    val distance : Long,
    val date : LocalDate,
    val time : Long,
    val startTime : LocalDateTime,
    val endTime : LocalDateTime,
    val calories : Long,
    val route : ArrayList<LatLng>
)