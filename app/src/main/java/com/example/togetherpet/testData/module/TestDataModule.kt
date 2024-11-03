package com.example.togetherpet.testData.module

import android.content.Context
import com.example.togetherpet.testData.dao.MissingDao
import com.example.togetherpet.testData.dao.UserDao
import com.example.togetherpet.testData.dataBase.TestDatabase
import com.example.togetherpet.testData.repository.MissingRepository
import com.example.togetherpet.testData.repository.UserRepository
import com.example.togetherpet.testData.repositoryImpl.MissingRepositoryImpl
import com.example.togetherpet.testData.repositoryImpl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TestDatabase {
        return TestDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(db: TestDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideMissingDao(db: TestDatabase): MissingDao = db.missingDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepositoryImpl(userDao)

    @Provides
    @Singleton
    fun provideMissingRepository(missingDao: MissingDao): MissingRepository =
        MissingRepositoryImpl(missingDao)
}