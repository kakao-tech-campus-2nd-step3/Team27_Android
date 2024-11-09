package com.example.togetherpet.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.togetherpet.data.database.MissingDataBase
import com.example.togetherpet.data.database.ReportDataBase

object DatabaseProvider {
    @Volatile
    private var missingDatabase: MissingDataBase? = null
    @Volatile
    private var reportDatabase: ReportDataBase? = null

    fun getMissingDatabase(context: Context): MissingDataBase {
        return missingDatabase ?: synchronized(this) {
            missingDatabase ?: Room.databaseBuilder(
                context.applicationContext,
                MissingDataBase::class.java,
                "missing_database"
            ).build().also {
                missingDatabase = it
                Log.d("DatabaseProvider", "MissingDataBase instance created")
            }
        }
    }

    fun getReportDatabase(context: Context): ReportDataBase {
        return reportDatabase ?: synchronized(this) {
            reportDatabase ?: Room.databaseBuilder(
                context.applicationContext,
                ReportDataBase::class.java,
                "report_database"
            ).build().also {
                reportDatabase = it
                Log.d("DatabaseProvider", "ReportDataBase instance created")
            }
        }
    }
}