package com.example.togetherpet.Login

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    // Todo : 나중에 수정해야함

    @POST("api/v1/token")
    suspend fun postLoginToken(
        @Body body: String
    ) : LoginResponseDto
}