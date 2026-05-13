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
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    object Error : SearchState() // ошибка сервера
}

class SearchViewModel(
    private val tracksRepository: com.practicum.myapplication.domain.TracksRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private var lastQuery: String = ""

    fun search(whatSearch: String) {
        lastQuery = whatSearch
        if (whatSearch.isEmpty()) {
            _searchScreenState.value = SearchState.Initial
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.update { SearchState.Searching }
            try {
                val list = tracksRepository.searchTracks(expression = whatSearch)
                if (list.isEmpty()) {
                    _searchScreenState.update { SearchState.Success(emptyList()) }
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