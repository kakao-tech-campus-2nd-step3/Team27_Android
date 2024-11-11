package com.example.togetherpet.data.dao

import androidx.room.*
import com.example.togetherpet.data.entity.ReportEntity

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reportEntities: List<ReportEntity>)

    @Query("SELECT * FROM report WHERE id = :reportId")
    suspend fun getReportById(reportId: Long): ReportEntity?

    @Update
    suspend fun updateReport(reportEntity: ReportEntity)

    @Query("SELECT id FROM report")
    suspend fun getAllIds(): List<Long>
}