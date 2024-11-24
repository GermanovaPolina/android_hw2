package com.homework.hw22.ui.components

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import java.io.File

@Composable
fun GifTile(context: Context, gifPath: String) {
    val cacheDir = context.cacheDir
    val gifsDir = File(cacheDir, "gifs")
    val gifFile = remember { File(gifsDir, gifPath) }

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    SubcomposeAsyncImage(
        model = gifFile,
        contentDescription = gifPath,
        imageLoader = imageLoader,
        loading = {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        },
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth()
    )
}