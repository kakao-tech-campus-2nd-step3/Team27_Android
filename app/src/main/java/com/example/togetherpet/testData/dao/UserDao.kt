package com.example.togetherpet.testData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.togetherpet.testData.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM user WHERE userNickname = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?
}