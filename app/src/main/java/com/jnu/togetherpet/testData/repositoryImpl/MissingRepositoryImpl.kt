package com.jnu.togetherpet.testData.repositoryImpl

import com.jnu.togetherpet.testData.dao.MissingDao
import com.jnu.togetherpet.testData.entity.Missing
import com.jnu.togetherpet.testData.repository.MissingRepository

class MissingRepositoryImpl(private val missingDao: MissingDao) : MissingRepository {
    override suspend fun insertMissingPet(missing: Missing) {
        missingDao.insert(missing)
    }

    override suspend fun getAllMissingPets(): List<Missing> {
        return missingDao.getAllMissingPets()
    }

    override suspend fun getMissingPetByName(name: String): Missing? {
        return missingDao.getMissingPetByName(name)
    }

    override suspend fun getMissingPetById(id: Int): Missing? {
        return missingDao.getMissingPetById(id)
    }
}