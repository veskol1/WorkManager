package com.example.workmanager

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface DownloadApi {

    @GET("/wp-content/uploads/2022/02/220849-scaled.jpg")
    suspend fun downloadImage(): Response<ResponseBody>

    companion object {
        val instance by lazy {
            Retrofit.Builder().baseUrl("https://pl-coding.com/").build()
                .create(DownloadApi::class.java)
        }
    }
}

