package com.practicum.myapplication.creator

import com.practicum.myapplication.data.network.RetrofitNetworkClient
import com.practicum.myapplication.data.network.TracksRepositoryImpl
import com.practicum.myapplication.domain.TracksRepository

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}