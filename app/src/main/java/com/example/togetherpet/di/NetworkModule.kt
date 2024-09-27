package com.example.togetherpet.di

import com.example.togetherpet.Login.LoginService
import com.example.togetherpet.PetService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    //todo : 수정 필요
    val BASE_URL = "https://exam.com/"

    @Provides
    @Singleton
    fun provideLoginService() : LoginService {
        return getApiClient().create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun providePetService() : PetService {
        return getApiClient().create(PetService::class.java)
    }

    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideLoginOkHttpClient(LoginInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginOkHttpClient(interceptor: LoginInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

    @Provides
    @Singleton
    fun provideLoginInterceptor(): LoginInterceptor {
        return LoginInterceptor()
    }

    class LoginInterceptor : Interceptor {
        // todo header 값 수정 필요
        val header = "header"

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader(header, header)
                .build()
            proceed(newRequest)
        }
    }
}
