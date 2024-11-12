package com.example.togetherpet.data.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ReportDetailResponseDTO(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("description") val description: String,
    @SerializedName("reporter_name") val reporterName: String,
    @SerializedName("image_url") val imageUrl: List<String>,
    @SerializedName("found_date") val foundDate: String //변경(11.12)
)
