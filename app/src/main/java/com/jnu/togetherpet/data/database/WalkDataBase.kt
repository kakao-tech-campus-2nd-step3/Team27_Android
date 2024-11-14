package com.jnu.togetherpet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jnu.togetherpet.data.dao.WalkDao
import com.jnu.togetherpet.data.entity.WalkEntity
import com.jnu.togetherpet.di.TypeConverterModule

@Database(entities = [WalkEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterModule::class)
abstract class WalkDataBase : RoomDatabase() {
    abstract fun walkDao(): WalkDao
}