package com.practicum.myapplication.domain

import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun saveTrack(track: Track)
    fun getTrackByNameAndArtist(track: Track): Flow<Track?>
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)
    suspend fun deleteTrackFromPlaylist(track: Track)
    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
    fun deleteTracksByPlaylistId(playlistId: Long)
    fun getTrackById(id: Long): Track?
    suspend fun getTrackByIdAsync(id: Long): Track?
    suspend fun removeTrackFromPlaylist(trackId: Long)
}