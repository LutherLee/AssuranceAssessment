package com.luther.github.data.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.luther.github.data.AppDatabase
import com.luther.github.data.model.MovieList
import com.luther.github.data.model.RemoteKey
import com.luther.github.data.network.NetworkResponse
import com.luther.github.data.network.service.MovieService
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator(
    private val movieTitle: String,
    private val appDatabase: AppDatabase,
    private val movieService: MovieService
) : RemoteMediator<Int, MovieList>() {
    private val movieDao = appDatabase.movieDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieList>
    ): MediatorResult {
        return try {
            // The network load method takes an optional String
            // parameter. For every page after the first, pass the String
            // token returned from the previous page to let it continue
            // from where it left off. For REFRESH, pass null to load the
            // first page.
            var loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                // Query remoteKeyDao for the next RemoteKey.
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(movieTitle)
                    }

                    // You must explicitly check if the page key is null when
                    // appending, since null is only valid for initial load.
                    // OmdbApi only allow search up to 100 page
                    if (remoteKey.nextKey > 100) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    remoteKey.nextKey
                }
            }

            if (loadKey == null) {
                loadKey = 1
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker thread.
            Timber.d("loadkey = $loadKey")
            val movieListResponse = movieService.searchMovieListByTitle(title = movieTitle, loadKey = loadKey)

            if (movieListResponse is NetworkResponse.Success) {
                val movieList = movieListResponse.body.search
                for (movie in movieList) {
                    movie.pageIndex = loadKey
                }

                // Store loaded data, and next key in transaction, so that
                // they're always consistent.
                appDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        remoteKeyDao.deleteByQuery(movieTitle)
                        movieDao.deleteByMovieTitle(movieTitle)
                    }

                    // Update RemoteKey for this query.
                    remoteKeyDao.insertOrReplace(
                        RemoteKey(movieTitle, loadKey + 1)
                    )

                    // Insert new users into database, which invalidates the
                    // current PagingData, allowing Paging to present the updates
                    // in the DB.
                    movieDao.insertList(movieList)
                }
                MediatorResult.Success(endOfPaginationReached = movieList.isEmpty())
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    /**
     * This method is where you can specify whether the cache data is up to date.
     * If it is up to date, use [InitializeAction.SKIP_INITIAL_REFRESH].
     * If you need to refresh cached data, use [InitializeAction.LAUNCH_INITIAL_REFRESH]
     */
    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}