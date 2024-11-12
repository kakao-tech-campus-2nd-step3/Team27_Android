package com.jnu.togetherpet.testData.repository

import com.jnu.togetherpet.testData.entity.Missing

interface MissingRepository {
    suspend fun insertMissingPet(missing: Missing)
    suspend fun getAllMissingPets(): List<Missing>
    suspend fun getMissingPetByName(name: String): Missing?
    suspend fun getMissingPetById(id: Int): Missing?
}