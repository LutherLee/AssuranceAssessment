package com.luther.github.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.luther.github.data.model.MovieDetailsOtherRatings

/**
 * Type converters to allow Room to reference complex data types.
 */
object Converters {
    @TypeConverter
    fun toMovieOtherRatingsList(data: String): List<MovieDetailsOtherRatings> {
        val listType = object : TypeToken<List<MovieDetailsOtherRatings>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toMovieOtherRatingsString(breed:  List<MovieDetailsOtherRatings>): String {
        return GsonBuilder().create().toJson(breed)
    }
}