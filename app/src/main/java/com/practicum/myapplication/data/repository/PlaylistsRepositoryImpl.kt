package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.db.AppDao
import com.practicum.myapplication.data.db.PlaylistEntity
import com.practicum.myapplication.domain.PlaylistsRepository
import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDao: AppDao) : PlaylistsRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDao.getAllPlaylistsWithTracks()
            .distinctUntilChanged()
            .map { list ->
                list.map { item ->
                    Playlist(
                        id = item.playlist.id,
                        name = item.playlist.name,
                        description = item.playlist.description,
                        coverPath = item.playlist.coverPath,
                        tracks = item.tracks.map { entity ->
                            Track(
                                id = entity.id,
                                trackName = entity.trackName,
                                artistName = entity.artistName,
                                trackTimeMillis = entity.trackTimeMillis,
                                artworkUrl100 = entity.artworkUrl100,
                                favorite = entity.isFavorite,
                                playlistId = entity.playlistId
                            )
                        }
                    )
                }
            }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return appDao.getPlaylistWithTracksById(playlistId)
            .distinctUntilChanged()
            .map { item ->
                item?.let {
                    Playlist(
                        id = it.playlist.id,
                        name = it.playlist.name,
                        description = it.playlist.description,
                        coverPath = it.playlist.coverPath,
                        tracks = it.tracks.map { entity ->
                            Track(
                                id = entity.id,
                                trackName = entity.trackName,
                                artistName = entity.artistName,
                                trackTimeMillis = entity.trackTimeMillis,
                                artworkUrl100 = entity.artworkUrl100,
                                favorite = entity.isFavorite,
                                playlistId = entity.playlistId
                            )
                        }
                    )
                }
            }
    }

    override suspend fun addNewPlaylist(name: String, description: String, coverPath: String) {
        appDao.insertPlaylist(PlaylistEntity(name = name, description = description, coverPath = coverPath))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDao.updatePlaylist(PlaylistEntity(playlist.id, playlist.name, playlist.description, playlist.coverPath))
    }

    override suspend fun deletePlaylistById(id: Long) {
        appDao.deletePlaylistById(id)
        appDao.deleteTracksByPlaylistId(id)
    }
}