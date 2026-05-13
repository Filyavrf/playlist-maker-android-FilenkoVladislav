package com.practicum.myapplication.data

import com.practicum.myapplication.domain.models.Playlist
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(private val scope: CoroutineScope) {
    companion object {
        private val historyList = mutableListOf<String>()
        private val _historyUpdates = MutableSharedFlow<Unit>()
        private val playlists = mutableListOf<Playlist>()
        private val tracks = mutableListOf(
            Track(1, "Владивосток 2000", "Мумий Троль", "02:38", 158000),
            Track(2, "Группа крови", "Кино", "04:43", 283000),
            Track(3, "Не смотри назад", "Ария", "05:12", 312000),
            Track(4, "Звезда по имени Солнце", "Кино", "03:45", 225000),
            Track(5, "Лондон", "Аквариум", "04:32", 272000),
            Track(6, "На заре", "Альянс", "03:50", 230000),
            Track(7, "Перемен", "Кино", "04:56", 296000),
            Track(8, "Розовый фламинго", "Сплин", "03:15", 195000),
            Track(9, "Танцевать", "Мельница", "03:42", 222000),
            Track(10, "Чёрный бумер", "Серега", "04:01", 241000)
        )
    }

    fun getHistory(): List<String> = historyList.toList()

    fun addToHistory(word: String) {
        historyList.add(word)
        notifyHistoryChanged()
    }

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val filteredPlaylists = mutableListOf<Playlist>()
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { track -> track.playlistId == playlist.id }
            filteredPlaylists.add(playlist.copy(tracks = playlistTracks))
        }
        emit(filteredPlaylists.toList())
        delay(100)
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == id })
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        val index = tracks.indexOfFirst { it.id == trackId }
        if (index != -1) {
            tracks[index] = tracks[index].copy(playlistId = 0)
        }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun getTrackById(id: Long): Track? {
        return tracks.find { it.id == id }
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        val favorites = tracks.filter { it.favorite }
        emit(favorites)
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.forEachIndexed { index, track ->
            if (track.playlistId == playlistId) {
                tracks[index] = track.copy(playlistId = 0)
            }
        }
    }

    fun searchTracks(expression: String): List<Track> {
        return tracks.filter { it.trackName.contains(expression, true) || it.artistName.contains(expression, true) }
    }
}