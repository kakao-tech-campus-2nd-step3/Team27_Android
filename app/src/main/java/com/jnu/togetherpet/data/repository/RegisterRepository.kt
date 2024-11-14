package com.jnu.togetherpet.data.repository

import android.util.Log
import com.jnu.togetherpet.data.datasource.RegisterSource
import com.jnu.togetherpet.data.dto.PetRegisterDTO
import com.jnu.togetherpet.exception.APIException
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
        try{
            Log.d("testt", " repo : ${petImage}, ${userName}")
            Log.d("testt", "token : ${tokenRepository.getTokenOrThrow()}")
            registerSource.registerUserAndPet(
                tokenRepository.getTokenOrThrow(),
                petRegisterDTO,
                petImage,
                userName
            )
        } catch (e : APIException){
            if (e.errorResponse.code == -20401) {
                registerSource.registerUserAndPet(
                    tokenRepository.getTokenOrThrow(),
                    PetRegisterDTO(petRegisterDTO.petNAme, petRegisterDTO.petBirthMonth, "말티즈", petRegisterDTO.isNeutering, petRegisterDTO.petFeature),
                    petImage,
                    userName
                )
                Log.d("testt", "보내기 :${PetRegisterDTO(petRegisterDTO.petNAme, petRegisterDTO.petBirthMonth, "말티즈", petRegisterDTO.isNeutering, petRegisterDTO.petFeature)}")
                Log.d("testt", "종없음 : ${e.hashCode()}, ${e.errorResponse.hashCode()}, ${e.message}")
            }
            else{
                Log.d("testt", "등록 에러 발생 :  ${e.hashCode()}, ${e.errorResponse.hashCode()}, ${e.message}")
            }
        }
    }
}