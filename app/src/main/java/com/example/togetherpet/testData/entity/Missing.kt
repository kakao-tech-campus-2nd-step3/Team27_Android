package com.example.togetherpet.testData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missing")
data class Missing(
    @PrimaryKey(autoGenerate = true) val missingId: Int = 0,
    @ColumnInfo val missingPetName: String,
    @ColumnInfo val missingDate: Int,
    @ColumnInfo val missingPlace: String,
    @ColumnInfo val missingPetImgUrl: String
)