package com.homework.hw22.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface GifApi {
    @GET("v1/gifs/random")
    suspend fun gif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String,
        @Header("x-rapidapi-key") xRapidApiKey: String,
        @Header("x-rapidapi-host") xRapidApiHost: String
    ): Response<Gif>

    @GET
    suspend fun gifBytes(@Url url: String): Response<ResponseBody>
}
