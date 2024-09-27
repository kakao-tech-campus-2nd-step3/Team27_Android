package com.example.togetherpet

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface PetRepository {
    suspend fun postPetInfo(
        name: String,
        petAge: Int,
        petSpecies: String,
        neutering: Boolean,
        petFeature: String,
        petImage: String
    ) : PetResponseDto
}