package com.luther.github.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is used in [MovieListRemoteMediator] that store the movieTitle search keyword
 * as [label] then based on that search keyword, we fetch the next page as [nextKey]
 */
@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val label: String,
    val nextKey: Int = 0
)