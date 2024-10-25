package com.example.togetherpet.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "report")
data class ReportEntity(
    @PrimaryKey val id: Number,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: MutableList<String>,
    val description: String?,
    val reporterName: String?,
    val foundDate: LocalDateTime?
)