package com.jnu.togetherpet

interface PetDataSource {
    suspend fun postPetInfo(petPostRequestDto: PetPostRequestDto) : PetResponseDto
}