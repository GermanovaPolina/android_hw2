package com.homework.hw22.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GifApi {
    @GET("v1/gifs/random")
    suspend fun gif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String,
        @Header("x-rapidapi-key") xRapidApiKey: String,
        @Header("x-rapidapi-host") xRapidApiHost: String
    ): Response<Gif>
}
