package com.example.togetherpet.data.service

import com.example.togetherpet.data.dto.PetRegisterDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterService {

    @Multipart
    @POST("/api/v1/register")
    suspend fun registerUserAndPet(
        @Part("PetRegisterDTO") petName: PetRegisterDTO,
        @Part("petImage") petImage: MultipartBody.Part,
        @Part("userName") userName: RequestBody
    ): Response<Unit>
}