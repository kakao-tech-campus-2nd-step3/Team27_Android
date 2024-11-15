package com.jnu.togetherpet.data.service

import com.jnu.togetherpet.data.dto.KakaoLocalResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalService {
    @GET("v2/local/geo/coord2address.json")
    suspend fun changeLatLngToAddress(
        @Header("Authorization") header: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String,
    ): Response<KakaoLocalResponseDTO>
}