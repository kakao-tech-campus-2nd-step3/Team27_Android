package com.jnu.togetherpet.testData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jnu.togetherpet.testData.entity.Missing

@Dao
interface MissingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(missing: Missing)

    @Query("SELECT * FROM missing")
    suspend fun getAllMissingPets(): List<Missing>

    @Query("SELECT * FROM missing WHERE missingPetName = :name LIMIT 1")
    suspend fun getMissingPetByName(name: String): Missing?

    @Query("SELECT * FROM missing WHERE missingId = :id LIMIT 1")
    suspend fun getMissingPetById(id: Int): Missing?
}