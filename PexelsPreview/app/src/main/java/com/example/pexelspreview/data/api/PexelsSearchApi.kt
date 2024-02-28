package com.example.pexelspreview.data.api

import com.example.pexelspreview.data.repuest.PexelsSearchRequestModel
import com.example.pexelspreview.data.response.ErrorResponseModel
import com.example.pexelspreview.data.response.PexelsSearchResponseModel
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

class PexelsSearchApi(private val requestModel: PexelsSearchRequestModel): Runnable {

    override fun run() {
        val client = OkHttpClient().newBuilder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }.build()
        try {
            val httpUrl = createHttpUrl()
            val request = Request.Builder().apply {
                header("Authorization", "fPY1Crh5axtAfHw3hGDsn0bBGxGUVGey1p8IdNKhdUNU82cXDbOBHZeV")
                url(httpUrl)
            }.build()
            val response = client.newCall(request).execute()
            if (response.code() != HttpsURLConnection.HTTP_OK && response.code() != HttpsURLConnection.HTTP_NO_CONTENT) {
                EventBus.getDefault().post(ErrorResponseModel(response.code().toString(), response.message()))
            } else {
                val responseModel = Gson().fromJson(response.body()?.string(), PexelsSearchResponseModel::class.java)
                EventBus.getDefault().post(responseModel)
            }
        } catch (ex: Exception) {
            EventBus.getDefault().post(ErrorResponseModel("network_error", "ネットワークエラー"))
        }
    }

    private fun createHttpUrl(): HttpUrl = HttpUrl.Builder().apply {
        scheme("https")
        host("api.pexels.com")
        addPathSegment("v1")
        addPathSegment("search")
        val json = Gson().toJson(requestModel)
        val obj = JSONObject(json)
        for (key in obj.keys()) {
            addQueryParameter(key, obj.get(key as String).toString())
        }
    }.build()

    // EventBusでAPIの呼び出し結果を通知するIF
    interface OnResultEvent {
        fun onPexelsSearchResult(result: PexelsSearchResponseModel)
        fun onErrorResult(result: ErrorResponseModel)
    }
}