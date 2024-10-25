package com.example.togetherpet.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missing")
data class MissingEntity(
    @PrimaryKey val id: Number,
    val petId: Number,
    val latitude: Double,
    val longitude: Double,
    // TODO 여기서 이미지가 중복으로 저장될 가능성이 보임
    val petImageUrl: MutableList<String>,
    val name: String?,
    val breed: String?,
    val birthMonth: Number?,
    val description: String?
)