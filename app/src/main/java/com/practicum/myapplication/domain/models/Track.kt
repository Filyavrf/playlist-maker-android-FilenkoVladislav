package com.practicum.myapplication.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Long = 0L,
    val artworkUrl100: String = "",
    val favorite: Boolean = false,
    val playlistId: Long = 0
) : Parcelable