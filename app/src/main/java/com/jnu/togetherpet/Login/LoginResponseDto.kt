package com.jnu.togetherpet.Login

import com.google.gson.annotations.SerializedName

// todo : 수정 필요
data class LoginResponseDto(
    @SerializedName("ExistToken") val existToken : Boolean
)
