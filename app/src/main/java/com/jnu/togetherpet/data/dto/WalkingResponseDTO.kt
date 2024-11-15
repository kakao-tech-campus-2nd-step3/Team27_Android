package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class WalkingResponseDTO(
    @SerializedName("location_list")val locationList: List<LocationDTO>,
    @SerializedName("walk_distance") val walkDistance: Float,
    @SerializedName("walk_time") val walkTime : Long,
    @SerializedName("walk_start_time_point") val walkStartTimePoint : String,
    @SerializedName("walk_end_time_point") val walkEndTimePoint : String
)
