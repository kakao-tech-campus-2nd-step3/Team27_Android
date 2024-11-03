package com.example.togetherpet

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

// todo : 수정 필요(미정)
data class PetResponseDto(
    @SerializedName("code") val code : String
)
