package com.homework.hw22.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ErrorTile(
    onErrorUpdate: (Boolean) -> Unit,
    onLoadingUpdate: (Boolean) -> Unit,
    onGifUpdate: (String) -> Unit,
) {
    return Card(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Произошла ошибка",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            LoadButton(
                onErrorUpdate = onErrorUpdate,
                onLoadingUpdate = onLoadingUpdate,
                onGifUpdate = onGifUpdate,
                text = "Перезагрузить гифку"
            )
        }
    }
}