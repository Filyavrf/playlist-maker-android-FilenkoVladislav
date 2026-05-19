package com.practicum.myapplication.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.creator.Creator
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SearchState {
    data class Initial(val history: List<String>) : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    object Empty : SearchState()
    object Error : SearchState()
}

class SearchViewModel(
    private val tracksRepository: com.practicum.myapplication.domain.TracksRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial(emptyList()))
    val searchScreenState = _searchScreenState.asStateFlow()

    private var lastQuery: String = ""

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val history = Creator.searchHistoryPreferences.getEntries()
            _searchScreenState.value = SearchState.Initial(history)
        }
    }

    fun search(whatSearch: String, saveToHistory: Boolean = true) {
        if (whatSearch.isEmpty()) {
            loadHistory()
            return
        }
        lastQuery = whatSearch
        if (saveToHistory) {
            viewModelScope.launch(Dispatchers.IO) {
                Creator.searchHistoryPreferences.addEntry(whatSearch)
            }
        }

        _searchScreenState.update { SearchState.Searching }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = tracksRepository.searchTracks(expression = whatSearch)
                if (list.isEmpty()) {
                    _searchScreenState.update { SearchState.Empty }
                } else {
                    _searchScreenState.update { SearchState.Success(list) }
                }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Error }
            }
        }
    }

    fun retryLastSearch() {
        if (lastQuery.isNotEmpty()) {
            search(lastQuery)
        }
    }

    fun saveTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.saveTrack(track)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            Creator.searchHistoryPreferences.clearHistory()
            loadHistory()
        }
    }

    fun addTrackToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.insertTrackToPlaylist(track, playlistId)
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.provideTracksRepository()) as T
                }
            }
    }
}