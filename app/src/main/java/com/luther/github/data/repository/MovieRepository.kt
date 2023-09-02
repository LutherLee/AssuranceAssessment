package com.luther.github.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luther.github.data.AppDatabase
import com.luther.github.data.dao.MovieDao
import com.luther.github.data.model.Movie
import com.luther.github.data.model.MovieList
import com.luther.github.data.network.NetworkResponse
import com.luther.github.data.network.mediator.MovieListRemoteMediator
import com.luther.github.data.network.model.MovieErrorResponse
import com.luther.github.data.network.service.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val movieDao: MovieDao,
    private val movieService: MovieService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun fetchPagedMovieListFromDatabase(
        movieTitle: String
    ): Flow<PagingData<MovieList>> {
        val pagingSourceFactory = when (movieTitle.isEmpty()) {
            true -> {{ movieDao.fetchAllMovieListFromDatabase() }}
            false -> { { movieDao.fetchMovieListFromDatabase(movieTitle) } }
        }
        return Pager(
            config = PagingConfig(pageSize = 15, enablePlaceholders = false),
            remoteMediator = MovieListRemoteMediator(movieTitle, appDatabase, movieService),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun searchMovieById(id: String): NetworkResponse<Movie, MovieErrorResponse> =
        movieService.searchMovieById(id = id)
}