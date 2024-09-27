package com.example.togetherpet

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindTokenDataSource(tokenDataSourceImpl: TokenDataSourceImpl) : TokenDataSource
    @Binds
    abstract fun bindPetDataSource(petDataSourceImpl: PetDataSourceImpl) : PetDataSource

}