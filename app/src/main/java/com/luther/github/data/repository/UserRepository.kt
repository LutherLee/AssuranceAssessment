package com.luther.github.data.repository

import com.luther.github.data.dao.UserDao
import com.luther.github.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun fetchUserByUsername(username: String): User? = userDao.fetchUserByUsername(username)

    suspend fun registerUser(user: User): Long = userDao.insert(user)
}