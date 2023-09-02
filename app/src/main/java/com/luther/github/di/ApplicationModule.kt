package com.luther.github.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.luther.github.Application.Companion.appContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.IOException
import java.security.GeneralSecurityException

/**
 * General application scope related modules
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private lateinit var masterKey: MasterKey
    private lateinit var sharedPreferences: SharedPreferences

    init {
        createEncryptedSharedPreferencesObject()
    }

    @Provides
    fun provideSharedPreferences(): SharedPreferences = sharedPreferences

    private fun createEncryptedSharedPreferencesObject() {
        try {
            masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            sharedPreferences = EncryptedSharedPreferences.create(
                appContext,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
