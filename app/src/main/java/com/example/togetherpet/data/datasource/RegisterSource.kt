package com.example.togetherpet.data.datasource

import com.example.togetherpet.data.dto.PetRegisterDTO
import com.example.togetherpet.data.service.RegisterService
import com.example.togetherpet.exception.APIException
import com.example.togetherpet.exception.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterSource @Inject constructor(
    private val registerService: RegisterService,
    private val gson: Gson
) {

    suspend fun registerUserAndPet(
        token: String,
        petRegisterDTO: PetRegisterDTO,
        petImage: File,
        userName: String
    ) {
        val response = registerService.registerUserAndPet(
            token,
            gson.toJson(petRegisterDTO).toRequestBody("application/json".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData(
                "petImage",
                petImage.name,
                petImage.asRequestBody("image/*".toMediaTypeOrNull())
            ),
            userName.toRequestBody("text/plain".toMediaTypeOrNull())
        )

        if (!response.isSuccessful) {
            throw APIException(
                gson.fromJson(
                    response.errorBody()?.string(),
                    ErrorResponse::class.java
                )
            )
        }
    }
}