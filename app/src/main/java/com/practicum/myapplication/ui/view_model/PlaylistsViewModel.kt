package com.practicum.myapplication.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistsViewModel : ViewModel() {

    private val playlistsRepository = Creator.providePlaylistsRepository()
    private val tracksRepository = Creator.provideTracksRepository()

    val playlists: StateFlow<List<Playlist>> = playlistsRepository
        .getAllPlaylists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteList: StateFlow<List<Track>> = tracksRepository
        .getFavoriteTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch { playlistsRepository.deletePlaylistById(playlist.id) }
    }

    fun addPlaylist(name: String, description: String) {
        viewModelScope.launch { playlistsRepository.addNewPlaylist(name, description) }
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
}