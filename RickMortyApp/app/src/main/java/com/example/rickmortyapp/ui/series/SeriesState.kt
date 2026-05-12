package com.example.rickmortyapp.ui.series

import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.model.PageInfo

import com.example.rickmortyapp.domain.model.CharacterFilter

import com.example.rickmortyapp.domain.model.EpisodeFilter

data class SeriesUiState(
    val selectedTab: HomeTab = HomeTab.Personajes,
    val characterFilter: CharacterFilter = CharacterFilter(),
    val episodeFilter: EpisodeFilter = EpisodeFilter(),
    val isFilterVisible: Boolean = false,
    val episodes: List<Episode> = emptyList(),
    val pageInfo: PageInfo? = null,
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val characters: List<Character> = emptyList(),
    val charactersPageInfo: PageInfo? = null,
    val charactersCurrentPage: Int = 1,
    val charactersLoading: Boolean = false,
    val charactersLoadingMore: Boolean = false,
    val charactersError: String? = null,
    val charactersRefreshing: Boolean = false
) {
    val hasNextPage: Boolean
        get() = pageInfo?.next != null

    val totalCount: Int
        get() = pageInfo?.count ?: 0

    val totalPages: Int
        get() = pageInfo?.pages ?: 0

    val charactersHasNextPage: Boolean
        get() = charactersPageInfo?.next != null

    val charactersTotalCount: Int
        get() = charactersPageInfo?.count ?: 0

    val charactersTotalPages: Int
        get() = charactersPageInfo?.pages ?: 0
}
