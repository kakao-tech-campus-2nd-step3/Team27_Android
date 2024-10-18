package com.example.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("email") val email: String
)