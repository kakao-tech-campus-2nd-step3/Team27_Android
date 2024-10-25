package com.example.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.togetherpet.data.dao.ReportDao
import com.example.togetherpet.data.entity.ReportEntity
import com.example.togetherpet.di.TypeConverterModule

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterModule::class)
abstract class ReportDataBase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}