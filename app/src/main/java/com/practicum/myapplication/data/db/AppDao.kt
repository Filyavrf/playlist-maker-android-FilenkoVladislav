package com.practicum.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table WHERE isFavorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE isFavorite = 1")
    suspend fun getFavoriteTracksSync(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE id = :trackId")
    suspend fun getTrackByIdSync(trackId: Long): TrackEntity?

    @Query("UPDATE track_table SET playlistId = 0 WHERE id = :trackId")
    suspend fun deleteTrackFromPlaylist(trackId: Long)

    @Query("UPDATE track_table SET playlistId = 0 WHERE playlistId = :playlistId")
    fun deleteTracksByPlaylistId(playlistId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE id = :id")
    fun getPlaylistWithTracksById(id: Long): Flow<PlaylistWithTracks?>

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)
}