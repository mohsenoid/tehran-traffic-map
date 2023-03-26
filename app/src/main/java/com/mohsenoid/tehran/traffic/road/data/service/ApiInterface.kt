package com.mohsenoid.tehran.traffic.road.data.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/wayonlinetraffic/{id}/")
    suspend fun getRoadMap(@Path("id") id: String): Response<ResponseBody>
}