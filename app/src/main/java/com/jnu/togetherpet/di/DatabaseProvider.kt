package com.jnu.togetherpet.di

import android.content.Context
import androidx.room.Room
import com.jnu.togetherpet.data.dao.MissingDao
import com.jnu.togetherpet.data.dao.ReportDao
import com.jnu.togetherpet.data.dao.WalkDao
import com.jnu.togetherpet.data.database.MissingDataBase
import com.jnu.togetherpet.data.database.ReportDataBase
import com.jnu.togetherpet.data.database.WalkDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {

    @Provides
    @Singleton
    fun provideMissingDatabase(@ApplicationContext context: Context): MissingDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            MissingDataBase::class.java,
            "missing_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDatabase(@ApplicationContext context: Context): ReportDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            ReportDataBase::class.java,
            "report_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WalkDataBase {
        return Room.databaseBuilder(
            context,
            WalkDataBase::class.java,
            "walking_database"
        ).build()
    }

    @Provides
    fun provideWalkDao(database: WalkDataBase): WalkDao {
        return database.walkDao()
    }

    @Provides
    fun provideMissingDao(database: MissingDataBase): MissingDao {
        return database.missingDao()
    }

    @Provides
    fun provideReportDao(database: ReportDataBase): ReportDao {
        return database.reportDao()
    }
}