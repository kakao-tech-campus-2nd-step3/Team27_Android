package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class MissingRegisterRequestDTO(
    @SerializedName("pet_name") val petName: String,
    @SerializedName("pet_gender") val petGender: String,
    @SerializedName("birth_month") val birthMonth: Long,
    @SerializedName("pet_breed") val breed: String,
    @SerializedName("lost_time") val lostTime: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("description") val description: String,
    @SerializedName("is_neutering") val isNeutering: Boolean,
)