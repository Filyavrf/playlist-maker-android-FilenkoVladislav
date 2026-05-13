package com.practicum.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel

@Composable
fun PlaylistsScreen(
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    onAddNewPlaylistClick: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.Black)
                }
                Text("Плейлисты", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewPlaylistClick,
                containerColor = Color(0xFF3772E7),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (playlists.isEmpty()) {
                Text(
                    text = "Ваша медиатека пуста",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(playlists.size) { index ->
                        PlaylistListItem(playlist = playlists[index], onClick = { /* Детали плейлиста */ })
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistListItem(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = playlist.name,
            modifier = Modifier.size(48.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(playlist.name, fontSize = 16.sp, color = Color.Black)
            Text("${playlist.tracks.size} треков", fontSize = 11.sp, color = Color.Gray)
        }
    }
}