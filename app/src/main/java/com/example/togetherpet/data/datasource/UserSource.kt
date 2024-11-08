package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.UserResponseDTO
import com.example.togetherpet.data.service.UserService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import javax.inject.Inject

class UserSource @Inject constructor(
    private val userService: UserService,
    private val gson: Gson
) {
    suspend fun getUserResponseDTO(token: String): UserResponseDTO? {
        val response = userService.getUserData(token)

        if (response.isSuccessful) {
            return response.body()
        }

        throw APIException(gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
    }
}