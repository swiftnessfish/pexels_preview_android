package com.example.pexelspreview.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponseModel(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)
