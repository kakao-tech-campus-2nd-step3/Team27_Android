package com.example.togetherpet

import com.google.gson.annotations.SerializedName

// todo : 수정 필요
data class PetPostRequestDto(
    @SerializedName("pet_name") val petName : String,
    @SerializedName("pet_birth_month") val petBirthMonth : Int,
    @SerializedName("pet_type") val petType : String,
    @SerializedName("is_neutering") val isNeutering : Boolean,
    @SerializedName("pet_feature") val petFeature : String,
    @SerializedName("pet_image") val petImage : String
)
