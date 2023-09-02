package com.luther.github.data.network.model

sealed class MovieType {
    abstract val keyword: String

    data object Movie : MovieType() {
        override val keyword: String = "movie"
    }

    data object Series : MovieType() {
        override val keyword: String = "series"
    }

    data object Episode : MovieType() {
        override val keyword: String = "episode"
    }
}
