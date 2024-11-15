package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class WalkingRequestDTO(
    @SerializedName("total_walk_distance") val distance : Float,
    @SerializedName("total_walk_time") val time : Long,
    @SerializedName("location_list") val location : List<LocationDTO>
)