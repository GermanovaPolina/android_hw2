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
import androidx.compose.material3.CircularProgressIndicator
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
import com.homework.hw22.ui.components.ErrorTile
import com.homework.hw22.ui.components.GifTile
import com.homework.hw22.ui.components.LoadButton

class MainActivity : ComponentActivity() {
    @Composable
    fun MainScreen() {
        var gif by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val onErrorUpdate = { e: Boolean -> error = e }
        val onLoadingUpdate = { l: Boolean -> loading = l }
        val onGifUpdate = { g: String -> gif = g }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            } else if (error) {
                ErrorTile(
                    onErrorUpdate,
                    onLoadingUpdate,
                    onGifUpdate
                )
            } else if (gif != "") {
                GifTile(context, gif = gif)
                Text(
                    text = gif,
                    color = if (error) Color.Red else Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoadButton(
                onErrorUpdate,
                onLoadingUpdate,
                onGifUpdate
            )
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
