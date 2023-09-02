package com.luther.github.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey
    val username: String = "",
    val password: String = ""
)