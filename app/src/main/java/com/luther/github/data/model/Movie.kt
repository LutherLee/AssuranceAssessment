package com.luther.github.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    @PrimaryKey
    @SerializedName("imdbID")
    val id: String = "",
    @SerializedName("Title") val title: String = "",
    @SerializedName("Poster") val posterUrl: String = "",
    @SerializedName("imdbVotes") val votes: String = "0",
    @SerializedName("imdbRating") val rating: String = "0",
    @SerializedName("Ratings") val otherRatings: List<MovieDetailsOtherRatings> = emptyList(),
    @SerializedName("Genre") val genre: String = "",
    @SerializedName("Plot") val plot: String = "",
)
