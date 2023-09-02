package com.luther.github.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.luther.github.data.model.MovieList

@Dao
interface MovieDao : BaseDao<MovieList> {
    @Query("SELECT * FROM movielist " +
            "WHERE title " +
            "LIKE '%' || :query || '%'" +
            "ORDER BY pageIndex ASC")
    fun fetchMovieListFromDatabase(query: String): PagingSource<Int, MovieList>

    @Query("SELECT * FROM movielist")
    fun fetchAllMovieListFromDatabase(): PagingSource<Int, MovieList>

    @Query("DELETE FROM movielist WHERE title = :title")
    suspend fun deleteByMovieTitle(title: String)
}