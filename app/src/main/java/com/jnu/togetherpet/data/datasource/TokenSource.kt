package com.jnu.togetherpet.data.datasource

interface TokenSource {
    fun saveToken(token: String)
    fun getTokenOrThrow(): String
    fun hasToken(): Boolean
}