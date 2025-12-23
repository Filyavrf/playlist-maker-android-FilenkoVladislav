package com.practicum.myapplication.domain

import com.practicum.myapplication.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}