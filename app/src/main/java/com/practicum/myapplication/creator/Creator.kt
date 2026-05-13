package com.practicum.myapplication.creator

import com.practicum.myapplication.data.network.NetworkClientImpl
import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.TracksRepository
import kotlinx.coroutines.GlobalScope

object Creator {
    fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            scope = GlobalScope,
            networkClient = NetworkClientImpl()
        )
    }
}