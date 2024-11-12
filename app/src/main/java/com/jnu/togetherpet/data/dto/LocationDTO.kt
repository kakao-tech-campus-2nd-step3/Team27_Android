package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double
)