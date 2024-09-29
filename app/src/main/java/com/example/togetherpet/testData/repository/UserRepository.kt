package com.example.togetherpet.testData.repository

import com.example.togetherpet.testData.entity.User

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUserById(id: Int): User?
}