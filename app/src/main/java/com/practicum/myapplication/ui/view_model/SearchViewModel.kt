package com.practicum.myapplication.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.repository.TracksRepositoryImpl
import com.practicum.myapplication.domain.TracksRepository
import com.practicum.myapplication.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    fun search(whatSearch: String) {
        if (whatSearch.isEmpty()) {
            _searchScreenState.value = SearchState.Initial
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Включаем лоадер
                _searchScreenState.update { SearchState.Searching }

                // 2. Делаем ту самую искусственную задержку в 1 секунду
                delay(1000L)

                // 3. Ищем треки
                val list = tracksRepository.searchTracks(expression = whatSearch)

                if (list.isEmpty()) {
                    _searchScreenState.update { SearchState.Success(foundList = emptyList()) }
                } else {
                    _searchScreenState.update { SearchState.Success(foundList = list) }
                }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    // Используем локальный мок-репозиторий
                    // Имитируем передачу скоупа, так как мы создали общий DatabaseMock, это сработает
                    val dummyScope = kotlinx.coroutines.GlobalScope
                    return SearchViewModel(TracksRepositoryImpl(dummyScope)) as T
                }
            }
    }
}