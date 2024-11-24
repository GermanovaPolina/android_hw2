package com.homework.hw22.api

import android.content.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface RequestController {
    suspend fun getGif(context: Context): ByteResult
}

sealed interface Result {
    data class Ok(val gif: Gif) : Result
    data class Error(val error: String) : Result
}

sealed interface ByteResult {
    data class ByteOk(val path: String) : ByteResult
    data class ByteError(val error: String) : ByteResult
}

@Serializable
data class Original(
    @SerialName("url") val url: String,
)

@Serializable
data class Images(
    @SerialName("original") val original: Original,
)

@Serializable
data class Data(
    @SerialName("id") val id: String,
    @SerialName("images") val images: Images,
)

@Serializable
data class Gif(
    @SerialName("data") val data: Data,
)