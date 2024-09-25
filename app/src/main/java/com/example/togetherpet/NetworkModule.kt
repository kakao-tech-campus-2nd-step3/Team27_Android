package com.example.togetherpet

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module

class NetworkModule {

    //todo : 수정 필요
    val BASE_URL = ""

    private fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        build()
    }

    class AppInterceptor : Interceptor {
        val header = ""

        override fun intercept(chain: Interceptor.Chain) : okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", header)
                .build()
            proceed(newRequest)
        }
    }
}
}