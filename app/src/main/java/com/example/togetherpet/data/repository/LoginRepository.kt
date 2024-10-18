package com.example.togetherpet.data.repository

import com.example.togetherpet.data.datasource.LoginSource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginSource: LoginSource
) {
    suspend fun login(email: String) {
        loginSource.login(email)
    }
}