package com.example.pexelspreview.data.api

import com.example.pexelspreview.data.repuest.PexelsSearchRequestModel
import java.util.concurrent.Executors

object ApiManager {

    private val executors = Executors.newFixedThreadPool(3)

    fun requestPexelsSearch(query: String, locale: String, page: Int, perPage: Int) {
        val requestModel = PexelsSearchRequestModel(query, locale, page, perPage)
        executors.submit(PexelsSearchApi(requestModel))
    }
}