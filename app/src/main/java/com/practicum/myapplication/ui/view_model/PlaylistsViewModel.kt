package com.practicum.myapplication.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.data.DatabaseMock
import com.practicum.myapplication.data.repository.PlaylistsRepositoryImpl
import com.practicum.myapplication.domain.PlaylistsRepository
import com.practicum.myapplication.domain.TracksRepository
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistsViewModel : ViewModel() {
    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(scope = viewModelScope)
    private val tracksRepository: TracksRepository = Creator.provideTracksRepository()
    private val databaseRepository: DatabaseMock = DatabaseMock(scope = viewModelScope)

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteList: Flow<List<Track>> = databaseRepository.getFavoriteTracks()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.insertTrackToPlaylist(track, playlistId)
        }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
        }
    }

    fun getTrackById(id: Long): Track? {
        return tracksRepository.getTrackById(id)
    }

    fun saveTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.saveTrack(track)
        }
    }
}