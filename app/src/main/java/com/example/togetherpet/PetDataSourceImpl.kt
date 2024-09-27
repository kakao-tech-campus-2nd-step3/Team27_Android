package com.example.togetherpet

import javax.inject.Inject

class PetDataSourceImpl @Inject constructor(private val petService: PetService) : PetDataSource {
    override suspend fun postPetInfo(petPostRequestDto: PetPostRequestDto): PetResponseDto {
        return petService.postPetInfo(petPostRequestDto)
    }
}