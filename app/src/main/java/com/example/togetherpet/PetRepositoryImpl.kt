package com.example.togetherpet

import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun postPetInfo(
        name: String,
        petAge: Int,
        petSpecies: String,
        neutering: Boolean,
        petFeature: String,
        petImage: String
    ) : PetResponseDto {
        val petPostRequestDto =
            PetPostRequestDto(name, petAge, petSpecies, neutering, petFeature, petImage)
        return petDataSource.postPetInfo(petPostRequestDto)
    }

}