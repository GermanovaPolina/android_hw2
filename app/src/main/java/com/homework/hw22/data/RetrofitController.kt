package com.homework.hw22.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitController(api: String) : RequestController {
    private val retrofit = Retrofit.Builder()
        .baseUrl(api)
        .addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
        )
        .build()

    private val gifApi = retrofit.create(GifApi::class.java)

    override suspend fun requestGif(): Result {
        val response = gifApi.gif(
            "2cq4m7p1zvSWMd1Q6EWJcNdBo28D7HSa", "arcane",
            xRapidApiKey = "530bd50c9fmsheb57b4ae0b6ba3ap1b5e8ejsn3026d0384a6f",
            xRapidApiHost = "giphy.p.rapidapi.com"
        )
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.Ok(it)
            } ?: Result.Error("No gif")
        } else {
            Result.Error(response.code().toString())
        }
    }
}
