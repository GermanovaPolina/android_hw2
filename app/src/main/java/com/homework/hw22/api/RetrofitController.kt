package com.homework.hw22.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import android.content.Context

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

    private suspend fun requestGifUrl(): Result {
        try {
            val response = gifApi.gif(
                "2cq4m7p1zvSWMd1Q6EWJcNdBo28D7HSa", "lara-croft-game",
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
        } catch (e: Exception) {
            return Result.Error("Unexpected error")
        }
    }

    private suspend fun downloadGif(context: Context, gifName: String, gifUrl: String): ByteResult {
        try {
            val response: Response<ResponseBody> = gifApi.gifBytes(gifUrl)
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    saveGifToFile(context, gifName, body)
                } else {
                    ByteResult.ByteError("No gif returned")
                }
            } else {
                return ByteResult.ByteError("Failed to download GIF bytes")
            }
        } catch (e: Exception) {
            return ByteResult.ByteError("Unexpected error")
        }
    }

    override
    suspend fun getGif(context: Context): ByteResult {
        return when (val result = requestGifUrl()) {
            is Result.Ok -> downloadGif(context, result.gif.data.id, result.gif.data.images.original.url)
            is Result.Error -> ByteResult.ByteError(result.error)
        }
    }
}

fun saveGifToFile(context: Context, gifName: String, responseBody: ResponseBody): ByteResult {
    try {
        val cacheDir = context.cacheDir
        val gifsDir = File(cacheDir, "gifs")
        gifsDir.mkdirs()
        val fileName = "${gifName}.gif"
        val gifFile = File(gifsDir, fileName)

        responseBody.byteStream().use { inputStream ->
            FileOutputStream(gifFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return ByteResult.ByteOk(fileName)
    } catch (e: Exception) {
        return ByteResult.ByteError("Couldn't save a file")
    }
}

