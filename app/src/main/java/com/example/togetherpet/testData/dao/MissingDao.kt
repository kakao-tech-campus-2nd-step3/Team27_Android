package com.example.togetherpet.testData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User

@Dao
interface MissingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(missing: Missing)

    @Query("SELECT * FROM missing")
    suspend fun getAllMissingPets(): List<Missing>

    @Query("SELECT * FROM missing WHERE missingPetName = :name LIMIT 1")
    suspend fun getMissingPetByName(name: String): Missing?
}