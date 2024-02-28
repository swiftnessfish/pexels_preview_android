package com.example.pexelspreview.data.response

import com.google.gson.annotations.SerializedName

data class PexelsSearchResponseModel(
    @SerializedName("photos")
    val photos: List<Photo>
) {

    data class Photo(
        @SerializedName("src")
        val src: Src,

        @SerializedName("photographer")
        val photographer: String
    )

    data class Src(
        @SerializedName("medium")
        val medium: String,

        @SerializedName("small")
        val small: String
    )
}