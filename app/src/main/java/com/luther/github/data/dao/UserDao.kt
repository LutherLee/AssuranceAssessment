package com.luther.github.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.luther.github.data.model.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun fetchUserByUsername(username: String): User?
}