package com.practicum.myapplication.creator

import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.TracksRepository
import kotlinx.coroutines.GlobalScope // Нужно импортировать

object Creator {

    fun provideTracksRepository(): TracksRepository {
        // Добавляем GlobalScope в качестве параметра
        return TracksRepositoryImpl(scope = GlobalScope)
    }
}