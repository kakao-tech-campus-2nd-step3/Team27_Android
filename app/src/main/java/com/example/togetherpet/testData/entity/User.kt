package com.example.togetherpet.testData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    @ColumnInfo val userNickname: String,
    @ColumnInfo val petName: String,
    @ColumnInfo val petImgUrl: String,
    @ColumnInfo val todayWalkCount: Int,
    @ColumnInfo val todayWalkDistance: Double,
    @ColumnInfo val todayWalkTime: String,
    @ColumnInfo val avgWalkCount: Int,
    @ColumnInfo val avgWalkDistance: Double,
    @ColumnInfo val avgWalkTime: String
)