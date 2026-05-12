package com.example.rickmortyapp.ui.series

import com.example.rickmortyapp.domain.model.CharacterFilter

import com.example.rickmortyapp.domain.model.EpisodeFilter

sealed interface SeriesUiEvent {
    data class SelectTab(val tab: HomeTab) : SeriesUiEvent
    data object ToggleFilterVisibility : SeriesUiEvent
    data class UpdateCharacterFilter(val filter: CharacterFilter) : SeriesUiEvent
    data class UpdateEpisodeFilter(val filter: EpisodeFilter) : SeriesUiEvent
    data object ClearFilter : SeriesUiEvent
    data object ApplyFilter : SeriesUiEvent
    data object LoadFirstPage : SeriesUiEvent
    data object LoadNextPage : SeriesUiEvent
    data object OnRefresh : SeriesUiEvent
    data object OnRetry : SeriesUiEvent
}
