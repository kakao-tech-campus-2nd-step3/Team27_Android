package com.example.togetherpet.data.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ReportCreateRequestDTO(
    @SerializedName("color") val color: String,
    @SerializedName("found_latitude") val foundLatitude: Double,
    @SerializedName("found_longitude") val foundLongitude: Double,
    @SerializedName("found_date") val foundDate: LocalDateTime,
    @SerializedName("description") val description: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("missing_id") val missingId: Long?,
)
