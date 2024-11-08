package com.example.togetherpet.di

import com.example.togetherpet.BuildConfig
import com.example.togetherpet.PetService
import com.example.togetherpet.data.service.KakaoLocalService
import com.example.togetherpet.data.service.LoginService
import com.example.togetherpet.data.service.MissingService
import com.example.togetherpet.data.service.RegisterService
import com.example.togetherpet.data.service.ReportService
import com.example.togetherpet.data.service.UserService
import com.example.togetherpet.data.service.WalkingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("kakaoLocalRetrofit")
    fun provideKakaoLocalRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterService(retrofit: Retrofit): RegisterService {
        return retrofit.create(RegisterService::class.java)
    }

    @Provides
    @Singleton
    fun provideMissingService(retrofit: Retrofit): MissingService {
        return retrofit.create(MissingService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportService(retrofit: Retrofit): ReportService {
        return retrofit.create(ReportService::class.java)
    }

    @Provides
    @Singleton
    fun providePetService(retrofit: Retrofit): PetService {
        return retrofit.create(PetService::class.java)
    }

    @Provides
    @Singleton
    fun provideWalkingService(retrofit: Retrofit): WalkingService {
        return retrofit.create(WalkingService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoLocalService(@Named("kakaoLocalRetrofit") retrofit: Retrofit): KakaoLocalService {
        return retrofit.create(KakaoLocalService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}
