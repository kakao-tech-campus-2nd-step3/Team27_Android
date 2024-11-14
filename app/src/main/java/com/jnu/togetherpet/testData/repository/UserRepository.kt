package com.jnu.togetherpet.testData.repository

import com.jnu.togetherpet.testData.entity.User

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByName(name: String): User?
}