package com.jnu.togetherpet.data.service

import com.jnu.togetherpet.data.dto.UserResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("/api/v1/user/info")
    suspend fun getUserData(
        @Header("Authorization")token : String
    ): Response<UserResponseDTO>
}