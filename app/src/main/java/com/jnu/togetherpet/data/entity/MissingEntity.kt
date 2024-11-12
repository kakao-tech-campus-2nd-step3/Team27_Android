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
    // TODO 여기서 이미지가 중복으로 저장될 가능성이 보임
    val petImageUrl: MutableList<String>,
    val name: String?,
    val breed: String?,
    val birthMonth: Long?,
    val description: String?
)