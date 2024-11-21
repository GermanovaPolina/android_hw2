package com.homework.hw22

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.homework.hw22.data.RetrofitController
import com.homework.hw22.data.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

class MainActivity : ComponentActivity() {
    val url = "https://media0.giphy.com/media/FXv82DjMG7T18nMIcy/giphy.gif?cid=76ddc56e3jyg8r3txaxzc2orwv0rx7o20owsr43182d6k5ki&ep=v1_gifs_random&rid=giphy.gif&ct=g"
    val url2 = "https://giphy.com/embed/FXv82DjMG7T18nMIcy"

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

        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = gif,
//                text = "hoi",
                color = if (error) Color.Red else Color.Black
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
