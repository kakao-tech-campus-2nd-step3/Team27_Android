package com.example.togetherpet.data.datasource

import android.util.Log
import com.example.togetherpet.BuildConfig
import com.example.togetherpet.data.dto.AddressDTO
import com.example.togetherpet.data.service.KakaoLocalService
import com.example.togetherpet.data.service.WalkingService
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KakaoLocalSource @Inject constructor(
    private val kakaoLocalService: KakaoLocalService
) {
    suspend fun latLngToAddress(longitude: String, latitude: String): List<AddressDTO> {
        val result = kakaoLocalService.changeLatLngToAddress(
            BuildConfig.KAKAO_LOCAL_API_KEY,
            longitude,
            latitude
        )
        Log.d("testt", "${result.body().toString()}, ${result.errorBody().toString()}")
        Log.d("testt", "Body: ${result.body().toString()}, Error: ${result.errorBody()?.string()}")
        return result.body()?.documents ?: emptyList()
    }
}
