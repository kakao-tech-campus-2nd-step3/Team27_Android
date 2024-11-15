package com.jnu.togetherpet.data.dao

import androidx.room.*
import com.jnu.togetherpet.data.entity.ReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reportEntities: List<ReportEntity>)

    @Query("SELECT * FROM report WHERE id = :reportId")
    suspend fun getReportById(reportId: Long): ReportEntity?

    @Update
    suspend fun updateReport(reportEntity: ReportEntity)

    // 내 반려동물 목격 제보 가져오기
    @Query("SELECT * FROM report WHERE isOwnReport = 1")
    fun getOwnReports(): Flow<List<ReportEntity>>

    // 근처 목격 제보 가져오기
    @Query("SELECT * FROM report WHERE isOwnReport = 0")
    fun getNearbyReports(): Flow<List<ReportEntity>>

    @Query("DELETE FROM report")
    fun deleteAllFromReportTable()
}