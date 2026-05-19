package com.practicum.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.practicum.myapplication.R
import com.practicum.myapplication.domain.models.Track
import com.practicum.myapplication.ui.view_model.SearchState
import com.practicum.myapplication.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    onTrackClick: (Track) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
) {
    val state by viewModel.searchScreenState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.back))
            }
            Text(stringResource(R.string.search), fontSize = 22.sp, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = ""
                        viewModel.search("")
                        keyboardController?.hide()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear_history))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchText.isNotEmpty()) {
                        viewModel.search(searchText)
                        keyboardController?.hide()
                    }
                }
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val currentState = state) {
                is SearchState.Initial -> {
                    HistorySection(
                        history = currentState.history,
                        onHistoryItemClick = { query ->
                            searchText = query
                            viewModel.search(query)
                            keyboardController?.hide()
                        },
                        onClearHistory = { viewModel.clearHistory() }
                    )
                }
                is SearchState.Searching -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
                }
                is SearchState.Success -> {
                    TrackList(
                        tracks = currentState.foundList,
                        onTrackClick = { track ->
                            viewModel.saveTrack(track)
                            onTrackClick(track)
                        }
                    )
                }
                is SearchState.Empty -> {
                    Placeholder(icon = Icons.Default.Info, text = stringResource(R.string.nothing_found))
                }
                is SearchState.Error -> {
                    Placeholder(
                        icon = Icons.Default.Warning,
                        text = stringResource(R.string.connection_error),
                        showRetry = true,
                        onRetry = { viewModel.retryLastSearch() }
                    )
                }
            }
        }
    }
}

@Composable
fun HistorySection(history: List<String>, onHistoryItemClick: (String) -> Unit, onClearHistory: () -> Unit) {
    if (history.isEmpty()) return
    Column(modifier = Modifier.fillMaxSize()) {
        Text(stringResource(R.string.you_searched), modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 12.dp), style = MaterialTheme.typography.titleMedium, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(history) { query ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onHistoryItemClick(query) }.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = query, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        Button(onClick = onClearHistory, modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)) { Text(stringResource(R.string.clear_history)) }
    }
}

@Composable
fun TrackList(tracks: List<Track>, onTrackClick: (Track) -> Unit) {
    LazyColumn {
        items(tracks) { track ->
            TrackItem(track = track, onClick = { onTrackClick(track) })
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 8.dp),
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
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(text = track.trackName, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = track.artistName, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                Text(text = " • ", fontSize = 11.sp, color = Color.Gray)
                Text(text = track.formattedDuration, fontSize = 11.sp, color = Color.Gray)
            }
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun Placeholder(icon: ImageVector, text: String, showRetry: Boolean = false, onRetry: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(120.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text, textAlign = androidx.compose.ui.text.style.TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        if (showRetry) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
        }
    }
}