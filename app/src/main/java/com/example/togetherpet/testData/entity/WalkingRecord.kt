package com.example.togetherpet.testData.entity

import com.kakao.vectormap.LatLng

data class WalkingRecord(
    val distance : Long,
    val date : Long,
    val time : Long,
    val baseTime : Long,
    val calories : Long,
    val route : ArrayList<LatLng>
)