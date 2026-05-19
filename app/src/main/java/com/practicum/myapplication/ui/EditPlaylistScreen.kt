package com.practicum.myapplication.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.models.Track
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistScreen(
    playlistId: Long,
    viewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val playlist by viewModel.getPlaylist(playlistId).collectAsStateWithLifecycle(null)
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedDescription by remember { mutableStateOf("") }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        coverUri = uri
    }

    LaunchedEffect(playlist) {
        playlist?.let {
            editedName = it.name
            editedDescription = it.description
            if (it.coverPath.isNotBlank()) {
                coverUri = Uri.parse(it.coverPath)
            } else {
                coverUri = null
            }
        }
    }

    if (playlist == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentPlaylist = playlist!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Редактирование" else currentPlaylist.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (isEditing) {
                            imagePickerLauncher.launch("image/*")
                        }
                    }
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                when {
                    coverUri != null -> {
                        AsyncImage(
                            model = coverUri,
                            contentDescription = "Cover",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    currentPlaylist.coverPath.isNotBlank() -> {
                        AsyncImage(
                            model = Uri.parse(currentPlaylist.coverPath),
                            contentDescription = "Cover",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = "Add cover",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                    }
                }
                if (isEditing) {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        color = Color.Black.copy(alpha = 0.6f),
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Change cover",
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            val updatedPlaylist = currentPlaylist.copy(
                                name = editedName,
                                description = editedDescription,
                                coverPath = coverUri?.toString() ?: currentPlaylist.coverPath
                            )
                            viewModel.updatePlaylist(updatedPlaylist)
                            isEditing = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = editedName.isNotBlank()
                ) {
                    Text("Сохранить")
                }
            } else {
                Text(
                    text = currentPlaylist.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${currentPlaylist.tracks.size} треков • ${formatDuration(currentPlaylist.tracks.sumOf { it.trackTimeMillis })}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3772E7))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Добавить треки")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentPlaylist.tracks) { track ->
                    TrackInPlaylistItem(
                        track = track,
                        onLongClick = {
                            scope.launch {
                                viewModel.removeTrackFromPlaylist(track.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackInPlaylistItem(track: Track, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.artworkUrl100,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_music),
            error = painterResource(R.drawable.ic_music),
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = " • ${track.trackTime}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}



private fun formatDuration(millis: Long): String {
    val minutes = (millis / 1000) / 60
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) "${hours}ч ${remainingMinutes}мин" else "${minutes}мин"
}