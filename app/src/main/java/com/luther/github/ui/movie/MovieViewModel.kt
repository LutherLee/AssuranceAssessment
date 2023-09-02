package com.luther.github.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luther.github.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
) : ViewModel() {

    companion object {
        const val MOVIE_SEARCH_KEYWORD = "movie_search_keyword"
        const val CURRENTLY_VIEWED_MOVIE_DETAILS_ID = "currently_viewed_movie_details"
    }

    /**
     * The search keyword user use to search for a list of movie by title.
     * Based on [_movieSearchKeyword] the movie list shown in [MovieListFragment] will alter
     */
    private val _movieSearchKeyword = savedStateHandle.getStateFlow(MOVIE_SEARCH_KEYWORD, "")
    val movieList = _movieSearchKeyword
        .flatMapLatest { movieTitle ->
            Timber.d("Fetching movie list from database")
            movieRepository.fetchPagedMovieListFromDatabase(movieTitle)
        }.cachedIn(viewModelScope)

    /**
     * The current movie details that the user are looking at
     */
    private val _currentlyViewedMovieDetailID =
        savedStateHandle.getStateFlow(CURRENTLY_VIEWED_MOVIE_DETAILS_ID, "")
    val movieDetails = _currentlyViewedMovieDetailID.mapLatest { movieID ->
        movieRepository.searchMovieById(movieID)
    }

    fun onSearchKeywordChanged(newQuery: String) {
        savedStateHandle[MOVIE_SEARCH_KEYWORD] = newQuery
    }

    fun onCurrentlyViewedMovieDetailChanged(movieID: String) {
        savedStateHandle[CURRENTLY_VIEWED_MOVIE_DETAILS_ID] = movieID
    }
}


