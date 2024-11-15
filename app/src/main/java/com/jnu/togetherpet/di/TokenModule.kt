package com.jnu.togetherpet.di

import com.jnu.togetherpet.data.datasource.TokenSource
import com.jnu.togetherpet.data.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenSource(
        tokenRepository: TokenRepository
    ): TokenSource
}