package com.practicum.myapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val isFavorite: Boolean,
    val playlistId: Long
)