package com.example.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.togetherpet.data.dao.ReportDao
import com.example.togetherpet.data.entity.ReportEntity

@Database(entities = [ReportEntity::class], version = 1)
abstract class ReportDataBase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}