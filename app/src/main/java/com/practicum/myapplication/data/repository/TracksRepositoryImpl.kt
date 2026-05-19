package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.db.AppDao
import com.practicum.myapplication.data.db.TrackEntity
import com.practicum.myapplication.data.dto.TracksSearchRequest
import com.practicum.myapplication.data.dto.TracksSearchResponse
import com.practicum.myapplication.domain.NetworkClient
import com.practicum.myapplication.domain.SearchHistoryRepository
import com.practicum.myapplication.domain.TracksRepository
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDao: AppDao,
    private val searchHistoryPreferences: SearchHistoryRepository
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200 && response is TracksSearchResponse) {
            val favs = appDao.getFavoriteTracksSync().map { it.id }
            return response.results.map { dto ->
                Track(
                    id = dto.trackId,
                    trackName = dto.trackName ?: "",
                    artistName = dto.artistName ?: "",
                    trackTimeMillis = dto.trackTimeMillis ?: 0,
                    artworkUrl100 = dto.artworkUrl100 ?: "",
                    favorite = favs.contains(dto.trackId),
                    playlistId = 0
                )
            }
        }
        return emptyList()
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDao.getFavoriteTracks().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val entity = track.copy(favorite = isFavorite).toEntity()
        appDao.insertTrack(entity)
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        val entity = track.copy(playlistId = playlistId).toEntity()
        appDao.insertTrack(entity)
    }

    override suspend fun saveTrack(track: Track) {
        val existing = appDao.getTrackByIdSync(track.id)
        if (existing == null) {
            appDao.insertTrack(track.toEntity())
        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        appDao.deleteTrackFromPlaylist(track.id)
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        appDao.deleteTracksByPlaylistId(playlistId)
    }

    override fun getTrackById(id: Long): Track? = null

    override suspend fun getTrackByIdAsync(id: Long): Track? {
        return appDao.getTrackByIdSync(id)?.toDomain()
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow { emit(null) }

    override suspend fun removeTrackFromPlaylist(trackId: Long) {
        appDao.removeTrackFromPlaylist(trackId)
    }

    private fun Track.toEntity(): TrackEntity {
        return TrackEntity(
            id = id,
            trackName = trackName,
            artistName = artistName,
            trackTime = "",
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            isFavorite = favorite,
            playlistId = playlistId
        )
    }

    private fun TrackEntity.toDomain(): Track {
        return Track(
            id = id,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            favorite = isFavorite,
            playlistId = playlistId
        )
    }
}