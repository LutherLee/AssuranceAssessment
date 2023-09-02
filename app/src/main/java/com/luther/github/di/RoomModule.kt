package com.luther.github.di

import android.content.Context
import androidx.room.Room
import com.luther.github.data.AppDatabase
import com.luther.github.data.dao.MovieDao
import com.luther.github.data.dao.RemoteKeyDao
import com.luther.github.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

/**
 * Module specifically for specifying how to supply Room related dependencies
 */
@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    private const val DATABASE_NAME = "sample-db"
    private const val PASSWORD_FOR_DATABASE = "Example"

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        // Realistically the passphrase should be obtained from the user or
        // auto generated by backend upon user login
        val passphrase: ByteArray = SQLiteDatabase.getBytes(PASSWORD_FOR_DATABASE.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideRemoteDao(appDatabase: AppDatabase): RemoteKeyDao {
        return appDatabase.remoteKeyDao()
    }
}