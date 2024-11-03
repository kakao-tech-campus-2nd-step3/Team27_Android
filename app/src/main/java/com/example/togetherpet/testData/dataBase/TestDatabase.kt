package com.example.togetherpet.testData.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.togetherpet.testData.dao.MissingDao
import com.example.togetherpet.testData.dao.UserDao
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User

@Database(entities = [User::class, Missing::class], version = 1, exportSchema = false)
abstract class TestDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun missingDao(): MissingDao

    companion object {
        private const val DATABASE_NAME = "testdata.db"

        @Volatile
        private var INSTANCE: TestDatabase? = null

        fun getInstance(context: Context): TestDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TestDatabase::class.java,
                        DATABASE_NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}