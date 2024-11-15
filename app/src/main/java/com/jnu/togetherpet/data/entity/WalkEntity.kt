package com.jnu.togetherpet.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "walking")
data class WalkEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    val walkDistance : Long,
    val walkTime: Long,
    val walkStartTime : LocalDateTime,
    val walkEndTime : LocalDateTime,
    val locationList: List<LatLng>,
    val walkCalories : Long,
    val walkDay : LocalDate
)