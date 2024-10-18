package com.example.togetherpet

import android.app.Application
import android.util.Log
import com.example.togetherpet.exception.APIException
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            if (exception is APIException) {
                Log.e("API Exception", "Error code : " + exception.errorResponse.code)
                Log.e("API Exception", "Error Message : " + exception.errorResponse.message)
            }
        }
    }
}