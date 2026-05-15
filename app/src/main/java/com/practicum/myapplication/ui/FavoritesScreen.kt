package com.practicum.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.ui.screens.TrackItem
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel

@Composable
fun FavoritesScreen(
    viewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (String) -> Unit
) {
    val favorites by viewModel.favoriteList.collectAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black) }
            Text("Избранные треки", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }

        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ваша медиатека пуста", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites.size) { index ->
                    val track = favorites[index]
                    TrackItem(
                        track = track,
                        onClick = {
                            viewModel.saveTrack(track)
                            onTrackClick(track.id.toString())
                        }
                    )
                }
            }
        }
    }
}