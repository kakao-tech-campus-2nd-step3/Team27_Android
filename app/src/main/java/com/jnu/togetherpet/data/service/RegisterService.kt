package com.jnu.togetherpet.data.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterService {

    @Multipart
    @POST("/api/v1/register")
    suspend fun registerUserAndPet(
        @Header("Authorization") token: String,
        @Part("PetRegisterDTO") petRegisterDTO: RequestBody,
        @Part petImage: MultipartBody.Part,
        @Part("userName") userName: RequestBody
    ): Response<Unit>


}