package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class ReportResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("image_url") val imageUrl: String,
)