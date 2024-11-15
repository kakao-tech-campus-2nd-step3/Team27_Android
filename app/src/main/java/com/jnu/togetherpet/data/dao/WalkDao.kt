package com.jnu.togetherpet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jnu.togetherpet.data.entity.WalkEntity
import java.time.LocalDate

@Dao
interface WalkDao {

    @Insert
    suspend fun insertWalk(walk: WalkEntity)

    @Query("SELECT * FROM walking WHERE walkDay = :day")
    suspend fun getWalksByDay(day: LocalDate): List<WalkEntity>
}