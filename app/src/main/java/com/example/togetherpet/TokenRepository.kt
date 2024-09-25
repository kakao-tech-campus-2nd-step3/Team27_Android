package com.example.togetherpet

import javax.inject.Inject

class TokenRepository @Inject constructor(private val tokenDataSource: TokenDataSource) {

}