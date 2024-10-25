package com.example.togetherpet.data.dao

import androidx.room.*
import com.example.togetherpet.data.entity.MissingEntity

@Dao
interface MissingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissing(missingEntities: List<MissingEntity>)

    @Query("SELECT * FROM missing WHERE id = :missingId")
    suspend fun getMissing(missingId: Number): MissingEntity?

    @Update(entity = MissingEntity::class)
    suspend fun updateMissing(missingEntity: MissingEntity)
}