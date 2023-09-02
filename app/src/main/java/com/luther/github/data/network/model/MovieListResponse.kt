package com.luther.github.data.network.model

import com.google.gson.annotations.SerializedName
import com.luther.github.data.model.MovieList

data class MovieListResponse(
    @SerializedName("Search")
    val search: List<MovieList> = emptyList(),
    @SerializedName("totalResults") val totalResults: Int = 0
)