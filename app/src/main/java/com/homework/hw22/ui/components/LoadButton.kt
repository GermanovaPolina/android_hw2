package com.homework.hw22.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.homework.hw22.MainActivity.Companion.API
import com.homework.hw22.api.RetrofitController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import com.homework.hw22.api.ByteResult

suspend fun processGifResult(gifResult: ByteResult, callback: (String, Boolean) -> Unit) {
    withContext(Dispatchers.IO) {
        when (gifResult) {
            is ByteResult.ByteOk -> {
                callback(gifResult.path, false)
            }
            is ByteResult.ByteError -> {
                callback("", true)
            }
        }
    }
}

@Composable
fun LoadButton(
    index: Int,
    onErrorUpdate: (Int, Boolean) -> Unit,
    onLoadingUpdate: (Int, Boolean) -> Unit,
    onGifUpdate: (Int, String) -> Unit,
    onStartLoading: () -> Unit,
    text: String = "Загрузить гифку",
    alignment: Alignment = Alignment.BottomCenter,
) {
    val context = LocalContext.current
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        onErrorUpdate(index, true)
    }

    val retrofitController by lazy {
        RetrofitController(API)
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp),
            onClick = {
                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    onStartLoading()
                    onLoadingUpdate(index, true)
                    onErrorUpdate(index, false)
                    val gifResult = retrofitController.getGif(context)
                    processGifResult(gifResult) { newGif, newError ->
                        onGifUpdate(index, newGif)
                        onErrorUpdate(index, newError)
                        onLoadingUpdate(index, false)
                    }
                }
            }) {
            Text(text, modifier = Modifier.padding(8.dp))
        }
    }
}
