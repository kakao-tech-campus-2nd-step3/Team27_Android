package com.example.togetherpet.testData.repositoryImpl

import com.example.togetherpet.testData.dao.UserDao
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User
import com.example.togetherpet.testData.repository.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    override suspend fun getUserByName(name: String): User? {
        return userDao.getUserByName(name)
    }
}