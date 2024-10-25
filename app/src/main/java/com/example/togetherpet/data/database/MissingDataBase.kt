package com.example.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.togetherpet.data.dao.MissingDao
import com.example.togetherpet.data.entity.MissingEntity
import com.example.togetherpet.di.TypeConverterModule

@Database(entities = [MissingEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterModule::class)
abstract class MissingDataBase : RoomDatabase() {
    abstract fun missingDao(): MissingDao
}