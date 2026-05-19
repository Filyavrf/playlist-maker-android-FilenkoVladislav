package com.practicum.myapplication.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistsViewModel : ViewModel() {

    private val playlistsRepository = Creator.providePlaylistsRepository()
    private val tracksRepository = Creator.provideTracksRepository()

    companion object {
        private const val WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS = 5000L
    }

    val playlists: StateFlow<List<Playlist>> = playlistsRepository
        .getAllPlaylists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    val favoriteList: StateFlow<List<Track>> = tracksRepository
        .getFavoriteTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch { playlistsRepository.deletePlaylistById(playlist.id) }
    }

    fun addPlaylist(name: String, description: String, coverPath: String = "") {
        viewModelScope.launch { playlistsRepository.addNewPlaylist(name, description, coverPath) }
    }

    fun saveTrack(track: Track) {
        viewModelScope.launch { tracksRepository.saveTrack(track) }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch { tracksRepository.updateTrackFavoriteStatus(track, isFavorite) }
    }

    fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch { tracksRepository.insertTrackToPlaylist(track, playlistId) }
    }

    suspend fun getTrackByIdAsync(id: Long): Track? {
        return tracksRepository.getTrackByIdAsync(id)
    }

    fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistsRepository.getPlaylist(playlistId)
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsRepository.updatePlaylist(playlist)
        }
    }

    fun removeTrackFromPlaylist(trackId: Long) {
        viewModelScope.launch {
            tracksRepository.removeTrackFromPlaylist(trackId)
        }
    }
}