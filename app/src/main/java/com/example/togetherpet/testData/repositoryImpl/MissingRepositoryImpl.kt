package com.example.togetherpet.testData.repositoryImpl

import com.example.togetherpet.testData.dao.MissingDao
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.repository.MissingRepository

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
}