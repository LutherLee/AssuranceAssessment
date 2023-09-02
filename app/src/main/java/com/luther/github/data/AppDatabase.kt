package com.luther.github.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luther.github.data.dao.MovieDao
import com.luther.github.data.dao.RemoteKeyDao
import com.luther.github.data.dao.UserDao
import com.luther.github.data.model.Movie
import com.luther.github.data.model.MovieList
import com.luther.github.data.model.RemoteKey
import com.luther.github.data.model.User

/**
 * The Room database for this app
 */
@Database(
    entities = [Movie::class, MovieList::class, User::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun userDao(): UserDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
