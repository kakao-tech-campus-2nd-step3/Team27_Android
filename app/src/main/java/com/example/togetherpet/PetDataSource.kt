package com.example.togetherpet

interface PetDataSource {
    suspend fun postPetInfo(petPostRequestDto: PetPostRequestDto) : PetResponseDto
}