package com.luther.github.util

/**
 * A generic class that holds a value with its loading status.
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T? = null) : Result<T>()
    data class Error(val error: String = "") : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
        }
    }
}

/**
 * Used mainly for testing to check if data return successfully and not null
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null
