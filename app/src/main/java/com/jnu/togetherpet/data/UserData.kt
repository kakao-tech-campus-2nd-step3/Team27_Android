package com.jnu.togetherpet.data

import android.net.Uri

data class UserData(
    val userName : String = "",
    val petName : String = "",
    val petImageUri : Uri = Uri.EMPTY,
    val petBirthMonth : Long = 0
)