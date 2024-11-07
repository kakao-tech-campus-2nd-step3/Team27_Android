package com.example.togetherpet.data.repository

import androidx.core.net.toUri
import com.example.togetherpet.data.UserData
import com.example.togetherpet.data.datasource.UserSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userSource: UserSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun getUserData(): UserData {
        val userDTO = userSource.getUserResponseDTO(tokenRepository.getTokenOrThrow())
        return if (userDTO != null) {
            UserData(
                userDTO.userName,
                userDTO.petName,
                userDTO.petImageURL.toUri(),
                userDTO.petBirthMonth
            )
        } else UserData()
    }
}