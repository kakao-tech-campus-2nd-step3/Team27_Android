package com.jnu.togetherpet.exception

class APIException(
    val errorResponse: ErrorResponse
) : Exception(errorResponse.message)