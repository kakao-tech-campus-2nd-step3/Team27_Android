package com.example.togetherpet

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}