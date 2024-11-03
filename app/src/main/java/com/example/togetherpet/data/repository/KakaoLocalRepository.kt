package com.example.togetherpet.data.repository

import android.util.Log
import com.example.togetherpet.data.datasource.KakaoLocalSource
import com.example.togetherpet.data.dto.AddressDTO
import com.example.togetherpet.data.service.KakaoLocalService
import com.kakao.vectormap.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KakaoLocalRepository @Inject constructor(
    private val kakaoLocalSource: KakaoLocalSource
) {
    suspend fun latLngToAddress(latLng: LatLng) : AddressDTO{
        Log.d("testt", "longitude : ${latLng.longitude}, latitude : ${latLng.latitude}")
        return kakaoLocalSource.latLngToAddress(latLng.longitude.toString(), latLng.latitude.toString()).first()
    }
}