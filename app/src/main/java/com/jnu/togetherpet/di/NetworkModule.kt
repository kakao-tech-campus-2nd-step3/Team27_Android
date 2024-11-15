package com.jnu.togetherpet.di

import com.jnu.togetherpet.BuildConfig
import com.jnu.togetherpet.data.service.KakaoLocalService
import com.jnu.togetherpet.data.service.LoginService
import com.jnu.togetherpet.data.service.MissingService
import com.jnu.togetherpet.data.service.RegisterService
import com.jnu.togetherpet.data.service.ReportService
import com.jnu.togetherpet.data.service.UserService
import com.jnu.togetherpet.data.service.WalkingService
import com.jnu.togetherpet.security.SelfSigningHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(selfSigningHelper: SelfSigningHelper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(provideOkHttpClient(selfSigningHelper))
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

    @Singleton
    @Provides
    fun provideOkHttpClient(
        selfSigningHelper: SelfSigningHelper
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .sslSocketFactory(
                selfSigningHelper.sslContext.socketFactory,
                selfSigningHelper.tmf.trustManagers[0] as X509TrustManager
            )
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}
