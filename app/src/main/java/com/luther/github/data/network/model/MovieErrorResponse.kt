package com.luther.github.data.network.model

import com.google.gson.annotations.SerializedName

data class MovieErrorResponse(
    @SerializedName("Response")
    val response: String = "",
    @SerializedName("Error")
    val error: String = ""
)


