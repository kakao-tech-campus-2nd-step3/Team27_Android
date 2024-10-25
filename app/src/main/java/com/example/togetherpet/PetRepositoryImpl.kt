package com.example.togetherpet

import android.util.Log
import okhttp3.MultipartBody
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun postPetInfo(
        name: String,
        petAge: Int,
        petSpecies: String,
        neutering: Boolean,
        petFeature: String,
        petImage: MultipartBody.Part
    ): PetResponseDto {
        val petPostRequestDto =
            PetPostRequestDto(name, petAge, petSpecies, neutering, petFeature, petImage)
        val result = runCatching {
            petDataSource.postPetInfo(petPostRequestDto)
        }.onSuccess {
            Log.d("testt", " ")
        }.onFailure {
            Log.d("testt", it.toString())
        }
        return PetResponseDto("mock")
    }

}