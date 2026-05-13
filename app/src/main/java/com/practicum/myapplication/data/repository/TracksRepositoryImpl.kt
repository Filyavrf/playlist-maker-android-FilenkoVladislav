package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.DatabaseMock
import com.practicum.myapplication.data.dto.TrackDto
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.NetworkClient
import com.practicum.myapplication.domain.TracksRepository
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val networkClient: NetworkClient
) : TracksRepository {

    private val database = DatabaseMock(scope = scope)

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200 && response is TracksSearchResponse) {
            return response.results.map { dto ->
                Track(
                    id = dto.trackId,
                    trackName = dto.trackName,
                    artistName = dto.artistName,
                    trackTime = formatTime(dto.trackTimeMillis),
                    trackTimeMillis = dto.trackTimeMillis,
                    artworkUrl100 = dto.artworkUrl100,
                    favorite = false,
                    playlistId = 0
                )
            }
        }
        return emptyList()
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> = database.getTrackByNameAndArtist(track)
    override fun getFavoriteTracks(): Flow<List<Track>> = database.getFavoriteTracks()
    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track.copy(playlistId = playlistId))
    }
    override suspend fun deleteTrackFromPlaylist(track: Track) = database.deleteTrackFromPlaylist(track.id)
    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }
    override fun deleteTracksByPlaylistId(playlistId: Long) = database.deleteTracksByPlaylistId(playlistId)
    override fun getTrackById(id: Long): Track? = database.getTrackById(id)

    override suspend fun saveTrack(track: Track) {
        database.addOrUpdateTrack(track)
    }
}