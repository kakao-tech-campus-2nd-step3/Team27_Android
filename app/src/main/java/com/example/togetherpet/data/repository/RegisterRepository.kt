package com.example.togetherpet.data.repository

import android.util.Log
import com.example.togetherpet.data.datasource.RegisterSource
import com.example.togetherpet.data.dto.PetRegisterDTO
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val registerSource: RegisterSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun registerUserAndPet(
        petRegisterDTO: PetRegisterDTO,
        petImage: File,
        userName: String
    ) {
        Log.d("testt", " repo : ${petImage}, ${userName}")
        Log.d("testt", "token : ${tokenRepository.getTokenOrThrow()}")
        registerSource.registerUserAndPet(
            tokenRepository.getTokenOrThrow(),
            petRegisterDTO,
            petImage,
            userName
        )
    }
}