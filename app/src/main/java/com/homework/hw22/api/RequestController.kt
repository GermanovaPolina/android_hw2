package com.homework.hw22.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface RequestController {
    suspend fun requestGif(): Result
}

sealed interface Result {
    data class Ok(val gif: Gif) : Result
    data class Error(val error: String) : Result
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
    @SerialName("images") val images: Images,
)

@Serializable
data class Gif(
    @SerialName("data") val data: Data,
)