package com.luther.github.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

/**
 * Base DAO implemented by other DAO with common use case
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(items: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: T): Long

    @Update
    suspend fun updateList(items: List<T>)

    @Update
    suspend fun update(item: T)

    @Delete
    suspend fun deleteList(items: List<T>)

    @Delete
    suspend fun delete(item: T)

    @Upsert
    suspend fun upsertList(items: List<T>)

    @Upsert
    suspend fun upsert(item: T)
}