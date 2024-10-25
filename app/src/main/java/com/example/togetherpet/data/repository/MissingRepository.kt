package com.example.togetherpet.data.repository

import android.content.Context
import androidx.room.Room
import com.example.togetherpet.data.dao.MissingDao
import com.example.togetherpet.data.database.MissingDataBase
import com.example.togetherpet.data.datasource.MissingSource
import com.example.togetherpet.data.dto.MissingRegisterRequestDTO
import com.example.togetherpet.data.entity.MissingEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MissingRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val missingSource: MissingSource,
    private val tokenRepository: TokenRepository
) {
    private val db: MissingDataBase = Room.databaseBuilder(
        context.applicationContext,
        MissingDataBase::class.java,
        "missing_database"
    ).build()

    private val missingDao: MissingDao = db.missingDao()

    suspend fun registerMissing(
        missingRegisterRequestDTO: MissingRegisterRequestDTO
    ) {
        missingSource.registerMissing(
            tokenRepository.getTokenOrThrow(),
            missingRegisterRequestDTO
        )
    }

    suspend fun getMissingNearBy(
        latitude: Double,
        longitude: Double
    ) {
        missingDao.insertMissing(
            missingSource.getMissingNearBy(latitude, longitude)
                .map { missing ->
                    MissingEntity(
                        missing.missingId,
                        missing.petId,
                        missing.latitude,
                        missing.longitude,
                        mutableListOf(missing.petImageUrl),
                        null,
                        null,
                        null,
                        null,
                    )
                }
        )
    }

    suspend fun getMissingByMissingId(
        missingId: Long
    ) {
        val findMissing = missingDao.getMissing(missingId)

        if (findMissing != null) {
            val detailMissing = missingSource.getMissingByMissingId(missingId)
            findMissing.petImageUrl.addAll(detailMissing.imageUrl)
            val updateMissing = findMissing.copy(
                name = detailMissing.name,
                breed = detailMissing.breed,
                birthMonth = detailMissing.birth_month,
                description = detailMissing.description,
            )
            missingDao.updateMissing(updateMissing)
        }

        // TODO Error 발생 로직 추가
    }
}