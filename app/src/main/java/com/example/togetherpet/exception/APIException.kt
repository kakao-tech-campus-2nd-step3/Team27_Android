package com.example.togetherpet.exception

class APIException(
    val errorResponse: ErrorResponse
) : Exception(errorResponse.message)