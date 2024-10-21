package com.example.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class MissingResponseDTO(
    @SerializedName("missing_id") val missingId: Number,
    @SerializedName("pet_id") val petId: Number,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("pet_image_url") val petImageUrl: String
)
