package com.example.togetherpet.testData.repository

import com.example.togetherpet.testData.entity.Missing

interface MissingRepository {
    suspend fun insertMissingPet(missing: Missing)
    suspend fun getMissingPetById(id: Int): Missing?
    suspend fun getAllMissingPets(): List<Missing>
}