package com.homework.hw22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.homework.hw22.data.RetrofitController
import com.homework.hw22.data.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.homework.hw22.ui.components.GifTile

class MainActivity : ComponentActivity() {
    private val retrofitController by lazy {
        RetrofitController(API)
    }

    @Composable
    fun MainScreen() {
        var gif by remember { mutableStateOf("https://media.giphy.com/media/26ufnwz3wDUli7GU0/giphy.gif") }
        var error by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            error = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GifTile(context, gif = gif)
            Text(
                text = gif,
                color = if (error) Color.Red else Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    val gifResult = retrofitController.requestGif()
                    processGifResult(gifResult) { newGif, newError ->
                        gif = newGif
                        error = newError
                    }
                }
            }) {
                Text("Загрузить гифку")
            }
        }
    }


    private suspend fun processGifResult(gifResult: Result, callback: (String, Boolean) -> Unit) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }

    companion object {
        const val API = "https://giphy.p.rapidapi.com"
    }
}
