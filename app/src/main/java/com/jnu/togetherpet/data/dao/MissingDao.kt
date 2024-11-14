package com.jnu.togetherpet.data.dao

import androidx.room.*
import com.jnu.togetherpet.data.entity.MissingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissing(missingEntities: List<MissingEntity>)

    @Query("SELECT * FROM missing WHERE id = :missingId")
    suspend fun getMissing(missingId: Long): MissingEntity?

    @Update(entity = MissingEntity::class)
    suspend fun updateMissing(missingEntity: MissingEntity)

    @Query("SELECT * FROM missing")
    fun getAllMissingReports(): Flow<List<MissingEntity>>

    @Query("DELETE FROM missing")
    fun deleteAllFromMissingTable()
}