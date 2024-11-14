package com.jnu.togetherpet.data.repository

import com.jnu.togetherpet.data.datasource.LoginSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val loginSource: LoginSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun login(email: String) {
        tokenRepository.saveToken(loginSource.login(email))
    }
}