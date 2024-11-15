package com.jnu.togetherpet.data.repository

import com.jnu.togetherpet.data.datasource.LoginSource
import com.jnu.togetherpet.data.datasource.TokenSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val loginSource: LoginSource,
    private val tokenSource: TokenSource
) {
    suspend fun login(email: String) {
        tokenSource.saveToken(loginSource.login(email))
    }
}