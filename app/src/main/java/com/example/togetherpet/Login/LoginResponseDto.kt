package com.example.togetherpet.Login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

// todo : 수정 필요
data class LoginResponseDto(
    @SerializedName("ExistToken") val existToken : Boolean
)
