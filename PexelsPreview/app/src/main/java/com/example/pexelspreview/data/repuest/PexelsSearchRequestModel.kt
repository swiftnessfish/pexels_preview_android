package com.example.pexelspreview.data.repuest

import com.google.gson.annotations.SerializedName

data class PexelsSearchRequestModel(
    @SerializedName("query")
    val query: String,

    @SerializedName("locale")
    val locale: String,

    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val perPage: Int
)