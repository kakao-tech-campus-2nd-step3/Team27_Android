package com.jnu.togetherpet.data.datasource

import com.google.gson.Gson
import com.jnu.togetherpet.data.dao.WalkDao
import com.jnu.togetherpet.data.entity.WalkEntity
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkingLocalSource @Inject constructor(
    private val walkDao: WalkDao,
    private val gson: Gson
) {
    suspend fun insertWalkingData(
        walkEntity: WalkEntity
    ) {
        walkDao.insertWalk(walkEntity)
    }

    suspend fun readWalkingDataWithDate(date: LocalDate): List<WalkEntity>? {
        return walkDao.getWalksByDay(date)
    }
}