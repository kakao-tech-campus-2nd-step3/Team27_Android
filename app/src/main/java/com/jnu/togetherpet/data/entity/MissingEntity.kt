package com.jnu.togetherpet.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missing")
data class MissingEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    val petId: Long,
    val latitude: Double,
    val longitude: Double,
    val petImageUrl: MutableList<String>,
    val name: String?,
    val breed: String?,
    val birthMonth: Long?,
    val description: String?
)