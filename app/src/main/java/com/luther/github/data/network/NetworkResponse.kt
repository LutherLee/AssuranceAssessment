package com.luther.github.data.network

import okio.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body.Represents the non-2xx responses, it also contains
     * the error body and the response status code
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Represents network failure such as no internet connection cases.
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * Represents unexpected exceptions occurred creating the request or processing the
     * response, for example JSON parsing issues.
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}