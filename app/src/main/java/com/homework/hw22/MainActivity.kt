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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.homework.hw22.data.GifListItem
import com.homework.hw22.ui.components.ErrorTile
import com.homework.hw22.ui.components.GifTile
import com.homework.hw22.ui.components.LoadButton
import com.homework.hw22.utils.getGifFiles

class MainActivity : ComponentActivity() {
    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val gifs = remember { mutableStateListOf<GifListItem>().apply { addAll(getGifFiles(context)) } }

        val onStartLoading = { val res = gifs.add(GifListItem(path = "", error = false, loading = false)) }
        val onErrorUpdate = { index: Int, e: Boolean -> gifs[index] = gifs[index].copy(error = e) }
        val onLoadingUpdate = { index: Int, l: Boolean -> gifs[index] = gifs[index].copy(loading = l) }
        val onGifUpdate = { index: Int, g: String -> gifs[index] = gifs[index].copy(path = g) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(gifs) { index, gif  ->
                    if (gif.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    } else if (gif.error) {
                        ErrorTile(
                            index,
                            onErrorUpdate,
                            onLoadingUpdate,
                            onGifUpdate
                        )
                    } else if (gif.path.isNotEmpty()) {
                        GifTile(context, gifPath = gif.path)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoadButton(
            gifs.size,
            onErrorUpdate,
            onLoadingUpdate,
            onGifUpdate,
            onStartLoading,
        )
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
