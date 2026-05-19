package com.practicum.myapplication.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun addEntry(word: String)
    suspend fun getEntries(): List<String>
    suspend fun clearHistory()
}