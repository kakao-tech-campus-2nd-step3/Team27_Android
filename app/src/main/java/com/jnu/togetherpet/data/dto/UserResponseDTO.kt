package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDTO(
    @SerializedName("user_name") val userName : String,
    @SerializedName("pet_name") val petName : String,
    @SerializedName("pet_image_url") val petImageURL : String,
    @SerializedName("pet_birth_month") val petBirthMonth : Long
)
