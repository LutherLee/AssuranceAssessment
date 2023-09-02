package com.luther.github.data.network.service

import com.luther.github.BuildConfig
import com.luther.github.data.model.Movie
import com.luther.github.data.network.NetworkResponse
import com.luther.github.data.network.model.MovieErrorResponse
import com.luther.github.data.network.model.MovieListResponse
import com.luther.github.data.network.model.MovieType
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("./")
    suspend fun searchMovieListByTitle(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("s") title: String,
        @Query("type") type: String = MovieType.Movie.keyword,
        @Query("page") loadKey: Int
    ): NetworkResponse<MovieListResponse, MovieErrorResponse>

    @GET("./")
    suspend fun searchMovieById(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("i") id: String
    ): NetworkResponse<Movie, MovieErrorResponse>
}

