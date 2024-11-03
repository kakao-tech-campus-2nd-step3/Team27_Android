package com.example.togetherpet

import retrofit2.http.Body
import retrofit2.http.POST

interface PetService {
    // Todo : 나중에 수정해야함

    @POST("api/pet")
    suspend fun postPetInfo(
        @Body petPostRequestDto: PetPostRequestDto
    ) : PetResponseDto
}