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

    @Provides
    @Singleton
    private fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideLoginOkHttpClient(LoginInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    private fun provideLoginOkHttpClient(interceptor: LoginInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

    @Provides
    @Singleton
    private fun provideLoginInterceptor(): LoginInterceptor {
        return LoginInterceptor()
    }

    class LoginInterceptor : Interceptor {
        // todo header 값 수정 필요
        val header = ""

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader(header, header)
                .build()
            proceed(newRequest)
        }
    }
}
