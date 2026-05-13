package com.practicum.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.models.Track
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackId: Long,
    viewModel: PlaylistsViewModel,
    onBackClick: () -> Unit
) {
    val track = remember(trackId) { viewModel.getTrackById(trackId) }

    var isFavorite by remember(track) { mutableStateOf(track?.favorite ?: false) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val playlists by viewModel.playlists.collectAsState(emptyList())

    if (track == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Трек не найден") }
        return
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 16.dp, start = 4.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = "Cover",
                modifier = Modifier.padding(top = 24.dp).size(200.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = track.trackName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.align(Alignment.Start))
            Text(text = track.artistName, fontSize = 16.sp, color = Color.Gray, modifier = Modifier.align(Alignment.Start))

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showBottomSheet = true }, modifier = Modifier.size(50.dp).background(Color(0xFFE6E6E6), CircleShape)) {
                    Icon(Icons.Default.Add, contentDescription = "В плейлист", tint = Color.Gray)
                }

                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        viewModel.toggleFavorite(track, isFavorite)
                    },
                    modifier = Modifier.size(50.dp).background(Color(0xFFE6E6E6), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Длительность", color = Color.Gray, fontSize = 14.sp)
                Text(track.trackTime, color = Color.Black, fontSize = 14.sp)
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                    Text("Добавить в плейлист", fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center)

                    if (playlists.isEmpty()) {
                        Text("Нет плейлистов", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center, color = Color.Gray)
                    } else {
                        LazyColumn {
                            items(playlists.size) { index ->
                                val playlist = playlists[index]
                                PlaylistListItem(
                                    playlist = playlist,
                                    onClick = {
                                        viewModel.insertTrackToPlaylist(track, playlist.id)
                                        showBottomSheet = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}