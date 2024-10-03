package com.example.togetherpet.testData.repository

import com.example.togetherpet.testData.entity.Missing

interface MissingRepository {
    suspend fun insertMissingPet(missing: Missing)
    suspend fun getAllMissingPets(): List<Missing>
    suspend fun getMissingPetByName(name: String): Missing?
}