package com.jnu.togetherpet.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report")
data class ReportEntity(
    @PrimaryKey val id: Long,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: MutableList<String>,
    val description: String?,
    val reporterName: String?,
    val foundDate: String?,
    val isOwnReport: Boolean
)