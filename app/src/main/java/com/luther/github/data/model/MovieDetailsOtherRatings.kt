package com.luther.github.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsOtherRatings(
    @SerializedName("Source") val source: String = "",
    @SerializedName("Value") val value: String = ""
)