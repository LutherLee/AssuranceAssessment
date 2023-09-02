package com.luther.github.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class MovieList(
    @PrimaryKey
    @SerializedName("imdbID")
    val id: String = "",
    @SerializedName("Title") val title: String = "",
    @SerializedName("Poster") val posterUrl: String = "",
    var pageIndex: Int
)