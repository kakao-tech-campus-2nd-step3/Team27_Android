package com.example.togetherpet

import retrofit2.http.Body
import retrofit2.http.POST

data class Response(
    val response : String
)

interface LoginService {
    // Todo : 나중에 수정해야함

    @POST("api/v1/token")
    suspend fun postLoginToken(
        @Body body: String
    ) : Response
}