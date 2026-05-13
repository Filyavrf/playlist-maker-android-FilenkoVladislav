package com.practicum.myapplication.domain

import com.practicum.myapplication.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addNewPlaylist(name: String, description: String)
    suspend fun deletePlaylistById(id: Long)
}