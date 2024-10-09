package com.example.togetherpet.di

import com.example.togetherpet.PetRepository
import com.example.togetherpet.PetRepositoryImpl
import com.example.togetherpet.TokenRepository
import com.example.togetherpet.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    abstract fun bindPetRepository(petRepositoryImpl: PetRepositoryImpl): PetRepository
}
