package com.homework.hw22.utils

import android.content.Context
import com.homework.hw22.data.GifListItem
import java.io.File

fun getGifFiles(context: Context): List<GifListItem> {
    val cacheDir = context.cacheDir
    val gifsDir = File(cacheDir, "gifs")

    return gifsDir.listFiles()?.toList()?.map {
        file -> GifListItem(path = file.name, error = false, loading = false)
    } ?: emptyList()
}
