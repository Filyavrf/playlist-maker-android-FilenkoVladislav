package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.db.AppDao
import com.practicum.myapplication.data.db.PlaylistEntity
import com.practicum.myapplication.domain.PlaylistsRepository
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDao: AppDao) : PlaylistsRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDao.getAllPlaylistsWithTracks().map { list ->
            list.map { item ->
                Playlist(
                    id = item.playlist.id,
                    name = item.playlist.name,
                    description = item.playlist.description,
                    tracks = item.tracks.map { entity ->
                        Track(entity.id, entity.trackName, entity.artistName, entity.trackTime, entity.trackTimeMillis, entity.artworkUrl100, entity.isFavorite, entity.playlistId)
                    }
                )
            }
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return appDao.getPlaylistWithTracksById(playlistId).map { item ->
            item?.let {
                Playlist(
                    id = it.playlist.id,
                    name = it.playlist.name,
                    description = it.playlist.description,
                    tracks = it.tracks.map { entity ->
                        Track(entity.id, entity.trackName, entity.artistName, entity.trackTime, entity.trackTimeMillis, entity.artworkUrl100, entity.isFavorite, entity.playlistId)
                    }
                )
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        appDao.insertPlaylist(PlaylistEntity(name = name, description = description))
    }

    override suspend fun deletePlaylistById(id: Long) {
        appDao.deletePlaylistById(id)
        appDao.deleteTracksByPlaylistId(id)
    }
}