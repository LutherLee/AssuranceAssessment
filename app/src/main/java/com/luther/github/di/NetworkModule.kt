package com.luther.github.di

import com.luther.github.BuildConfig
import com.luther.github.data.network.exception.NetworkResponseAdapterFactory
import com.luther.github.data.network.service.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module specifically for specifying how to supply network related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://www.omdbapi.com/"

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Factory =  GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Factory
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideUserDetailsService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}