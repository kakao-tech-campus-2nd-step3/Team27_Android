package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class PetRegisterDTO(
    @SerializedName("pet_name") val petNAme: String,
    @SerializedName("pet_birth_month") val petBirthMonth: Long,
    @SerializedName("pet_type") val petType: String,
    @SerializedName("is_neutering") val isNeutering: Boolean,
    @SerializedName("pet_feature") val petFeature: String
)