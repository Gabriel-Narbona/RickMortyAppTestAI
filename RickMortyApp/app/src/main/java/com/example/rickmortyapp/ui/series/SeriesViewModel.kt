package com.example.rickmortyapp.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.CharacterFilter
import com.example.rickmortyapp.domain.model.EpisodeFilter
import com.example.rickmortyapp.domain.usecase.GetCharactersUseCase
import com.example.rickmortyapp.domain.usecase.GetEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SeriesUiState())
    val state: StateFlow<SeriesUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SeriesEffect>(replay = 0, extraBufferCapacity = 2)
    val effects = _effects.asSharedFlow()

    fun handleEvent(event: SeriesUiEvent) {
        when (event) {
            is SeriesUiEvent.SelectTab -> {
                _state.update { it.copy(selectedTab = event.tab, error = null, charactersError = null) }
                when (event.tab) {
                    HomeTab.Personajes -> if (_state.value.characters.isEmpty() && !_state.value.charactersLoading) {
                        loadCharacters(page = 1, isRefresh = false)
                    }
                    HomeTab.Capítulos -> if (_state.value.episodes.isEmpty() && !_state.value.isLoading) {
                        loadEpisodes(page = 1, isRefresh = false)
                    }
                }
            }
            is SeriesUiEvent.ToggleFilterVisibility -> _state.update { it.copy(isFilterVisible = !it.isFilterVisible) }
            is SeriesUiEvent.UpdateCharacterFilter -> _state.update { it.copy(characterFilter = event.filter) }
            is SeriesUiEvent.UpdateEpisodeFilter -> _state.update { it.copy(episodeFilter = event.filter) }
            is SeriesUiEvent.ClearFilter -> {
                when (_state.value.selectedTab) {
                    HomeTab.Personajes -> {
                        _state.update { it.copy(characterFilter = CharacterFilter()) }
                        loadCharacters(page = 1, isRefresh = true)
                    }
                    HomeTab.Capítulos -> {
                        _state.update { it.copy(episodeFilter = EpisodeFilter()) }
                        loadEpisodes(page = 1, isRefresh = true)
                    }
                }
            }
            is SeriesUiEvent.ApplyFilter -> {
                _state.update { it.copy(isFilterVisible = false) }
                when (_state.value.selectedTab) {
                    HomeTab.Personajes -> loadCharacters(page = 1, isRefresh = true)
                    HomeTab.Capítulos -> loadEpisodes(page = 1, isRefresh = true)
                }
            }
            is SeriesUiEvent.LoadFirstPage -> when (_state.value.selectedTab) {
                HomeTab.Personajes -> loadCharacters(page = 1, isRefresh = false)
                HomeTab.Capítulos -> loadEpisodes(page = 1, isRefresh = false)
            }
            is SeriesUiEvent.LoadNextPage -> when (_state.value.selectedTab) {
                HomeTab.Personajes -> {
                    val c = _state.value
                    if (!c.charactersLoadingMore && c.charactersHasNextPage) {
                        loadCharacters(page = c.charactersCurrentPage + 1, isLoadMore = true)
                    }
                }
                HomeTab.Capítulos -> {
                    val c = _state.value
                    if (!c.isLoadingMore && c.hasNextPage) {
                        loadEpisodes(page = c.currentPage + 1, isLoadMore = true)
                    }
                }
            }
            is SeriesUiEvent.OnRefresh -> when (_state.value.selectedTab) {
                HomeTab.Personajes -> loadCharacters(page = 1, isRefresh = true)
                HomeTab.Capítulos -> loadEpisodes(page = 1, isRefresh = true)
            }
            is SeriesUiEvent.OnRetry -> when (_state.value.selectedTab) {
                HomeTab.Personajes -> loadCharacters(page = 1, isRefresh = false)
                HomeTab.Capítulos -> loadEpisodes(page = 1, isRefresh = false)
            }
        }
    }

    private fun loadEpisodes(
        page: Int,
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        viewModelScope.launch {
            if (isLoadMore) {
                _state.update { it.copy(isLoadingMore = true, error = null) }
            } else if (isRefresh) {
                _state.update { it.copy(isRefreshing = true, error = null) }
            } else {
                _state.update { it.copy(isLoading = true, error = null) }
            }

            when (val result = getEpisodesUseCase(page, _state.value.episodeFilter)) {
                is Result.Success -> _state.update {
                    it.copy(
                        episodes = if (isLoadMore) it.episodes + result.data.results else result.data.results,
                        pageInfo = result.data.info,
                        currentPage = page,
                        isLoading = false,
                        isLoadingMore = false,
                        isRefreshing = false,
                        error = null
                    )
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false,
                            error = result.exception.message ?: "Error al cargar episodios"
                        )
                    }
                    _effects.emit(SeriesEffect.ShowError(result.exception.message ?: "Error al cargar episodios"))
                }
            }
        }
    }

    private fun loadCharacters(
        page: Int,
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        viewModelScope.launch {
            if (isLoadMore) {
                _state.update { it.copy(charactersLoadingMore = true, charactersError = null) }
            } else if (isRefresh) {
                _state.update { it.copy(charactersRefreshing = true, charactersError = null) }
            } else {
                _state.update { it.copy(charactersLoading = true, charactersError = null) }
            }

            when (val result = getCharactersUseCase(page, _state.value.characterFilter)) {
                is Result.Success -> _state.update {
                    it.copy(
                        characters = if (isLoadMore) it.characters + result.data.results else result.data.results,
                        charactersPageInfo = result.data.info,
                        charactersCurrentPage = page,
                        charactersLoading = false,
                        charactersLoadingMore = false,
                        charactersRefreshing = false,
                        charactersError = null
                    )
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            charactersLoading = false,
                            charactersLoadingMore = false,
                            charactersRefreshing = false,
                            charactersError = result.exception.message ?: "Error al cargar personajes"
                        )
                    }
                    _effects.emit(SeriesEffect.ShowError(result.exception.message ?: "Error al cargar personajes"))
                }
            }
        }
    }
}
