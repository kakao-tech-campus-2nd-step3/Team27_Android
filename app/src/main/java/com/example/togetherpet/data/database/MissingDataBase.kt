package com.example.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.togetherpet.data.dao.MissingDao
import com.example.togetherpet.data.entity.MissingEntity

@Database(entities = [MissingEntity::class], version = 1)
abstract class MissingDataBase : RoomDatabase() {
    abstract fun missingDao(): MissingDao
}