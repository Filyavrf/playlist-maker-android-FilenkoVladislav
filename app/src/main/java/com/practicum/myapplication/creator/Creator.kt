package com.practicum.myapplication.creator

import android.content.Context
import androidx.room.Room
import com.practicum.myapplication.data.db.AppDatabase
import com.practicum.myapplication.data.network.NetworkClientImpl
import com.practicum.myapplication.data.preferences.SearchHistoryPreferences
import com.practicum.myapplication.data.repository.PlaylistsRepositoryImpl
import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.PlaylistsRepository
import com.practicum.myapplication.domain.TracksRepository

object Creator {
    private lateinit var database: AppDatabase
    lateinit var searchHistoryPreferences: SearchHistoryPreferences

    fun init(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, "playlist_maker_db")
            .fallbackToDestructiveMigration()
            .build()
        searchHistoryPreferences = SearchHistoryPreferences(context)
    }

    fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(NetworkClientImpl(), database.appDao(), searchHistoryPreferences)
    }

    fun providePlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(database.appDao())
    }
}