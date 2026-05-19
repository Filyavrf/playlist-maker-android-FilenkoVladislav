package com.practicum.myapplication.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long = 0L,
    val artworkUrl100: String = "",
    val favorite: Boolean = false,
    val playlistId: Long = 0
) : Parcelable {
    val formattedDuration: String
        get() {
            val minutes = (trackTimeMillis / 1000) / 60
            val seconds = (trackTimeMillis / 1000) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
}