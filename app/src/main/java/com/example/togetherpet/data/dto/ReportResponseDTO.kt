package com.example.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class ReportResponseDTO(
    @SerializedName("id") val id: Number,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("image_url") val imageUrl: String,
)
