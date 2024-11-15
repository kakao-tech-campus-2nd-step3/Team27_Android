package com.jnu.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jnu.togetherpet.data.dao.ReportDao
import com.jnu.togetherpet.data.entity.ReportEntity
import com.jnu.togetherpet.di.TypeConverterModule

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterModule::class)
abstract class ReportDataBase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}