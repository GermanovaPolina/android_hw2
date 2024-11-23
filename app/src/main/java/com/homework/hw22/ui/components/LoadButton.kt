package com.homework.hw22.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.homework.hw22.MainActivity.Companion.API
import com.homework.hw22.data.Result
import com.homework.hw22.data.RetrofitController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun processGifResult(gifResult: Result, callback: (String, Boolean) -> Unit) {
    withContext(Dispatchers.Main) {
        when (gifResult) {
            is Result.Ok -> {
                callback(gifResult.gif.data.images.original.url, false)
            }
            is Result.Error -> {
                callback("Error: ${gifResult.error}", true)
            }
        }
    }
}

@Composable
fun LoadButton(
    onErrorUpdate: (Boolean) -> Unit,
    onLoadingUpdate: (Boolean) -> Unit,
    onGifUpdate: (String) -> Unit,
    text: String = "Загрузить гифку",
) {
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        onErrorUpdate(true)
    }

    val retrofitController by lazy {
        RetrofitController(API)
    }

    return Button(onClick = {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            onLoadingUpdate(true)
            onErrorUpdate(false)
            val gifResult = retrofitController.requestGif()
            processGifResult(gifResult) { newGif, newError ->
                onGifUpdate(newGif)
                onErrorUpdate(newError)
                onLoadingUpdate(false)
            }
        }
    }) {
        Text(text)
    }
}
