package com.example.rickmortyapp.ui.character.detail

import com.example.rickmortyapp.domain.model.Character

data class CharacterDetailUiState(
    val character: Character? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
