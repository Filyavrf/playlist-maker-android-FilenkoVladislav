package com.practicum.myapplication.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)