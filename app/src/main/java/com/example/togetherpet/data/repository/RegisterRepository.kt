package com.example.togetherpet.data.repository

import com.example.togetherpet.data.datasource.RegisterSource
import com.example.togetherpet.data.dto.PetRegisterDTO
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val registerSource: RegisterSource
) {
    suspend fun registerUserAndPet(
        petRegisterDTO: PetRegisterDTO,
        petImage: File,
        userName: String
    ) {
        registerSource.registerUserAndPet(petRegisterDTO, petImage, userName)
    }
}