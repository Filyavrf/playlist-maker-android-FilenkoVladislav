package com.practicum.myapplication.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

class SearchHistoryPreferences(
    private val context: Context,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history") + SupervisorJob() + Dispatchers.IO)
) {
    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
        private val HISTORY_KEY = stringPreferencesKey("search_history_key")
    }

    fun addEntry(word: String) {
        if (word.isEmpty()) return
        coroutineScope.launch {
            context.dataStore.edit { preferences ->
                val historyString = preferences[HISTORY_KEY].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }
                history.remove(word)
                history.add(0, word)
                val subList = history.take(MAX_ENTRIES)
                preferences[HISTORY_KEY] = subList.joinToString(SEPARATOR)
            }
        }
    }

    suspend fun getEntries(): List<String> {
        val preferences = context.dataStore.data.first()
        val historyString = preferences[HISTORY_KEY].orEmpty()
        return if (historyString.isNotEmpty()) {
            historyString.split(SEPARATOR)
        } else {
            emptyList()
        }
    }

    suspend fun clearHistory() {
        context.dataStore.edit { preferences ->
            preferences.remove(HISTORY_KEY)
        }
    }
}