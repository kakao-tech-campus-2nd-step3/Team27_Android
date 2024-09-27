package com.example.togetherpet

import android.util.Log
import javax.inject.Inject

class PetDataSourceImpl @Inject constructor(private val petService: PetService) : PetDataSource {
    override suspend fun postPetInfo(petPostRequestDto: PetPostRequestDto): PetResponseDto {
        Log.d("testt", petPostRequestDto.toString())
        return petService.postPetInfo(petPostRequestDto)
    }
}