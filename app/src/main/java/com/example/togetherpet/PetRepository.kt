package com.example.togetherpet

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MultipartBody

interface PetRepository {
    suspend fun postPetInfo(
        name: String,
        petAge: Int,
        petSpecies: String,
        neutering: Boolean,
        petFeature: String,
        petImage: MultipartBody.Part
    ) : PetResponseDto
}