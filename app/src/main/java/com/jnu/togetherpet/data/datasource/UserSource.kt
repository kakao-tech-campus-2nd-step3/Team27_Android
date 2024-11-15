package com.jnu.togetherpet.data.datasource

import android.util.Log
import com.jnu.togetherpet.data.dto.UserResponseDTO
import com.jnu.togetherpet.data.service.UserService
import com.jnu.togetherpet.exception.APIException
import com.jnu.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import javax.inject.Inject

class UserSource @Inject constructor(
    private val userService: UserService,
    private val gson: Gson
) {
    suspend fun getUserResponseDTO(token: String): UserResponseDTO? {
        val response = userService.getUserData(token)
        Log.d("testt", "User : ${response.body()}, ${response.errorBody()}")
        if (response.isSuccessful) {
            return response.body()
        }

        throw APIException(gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
    }
}