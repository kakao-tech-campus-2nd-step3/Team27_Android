package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class MissingDetailResponseDTO(
    @SerializedName("name") val name: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("birth_month") val birthMonth: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("description") val description: String,
    @SerializedName("image_url") val imageUrl: List<String>
)
