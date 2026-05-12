package com.example.rickmortyapp.ui.character.edit

import com.example.rickmortyapp.domain.model.Character

data class EditCharacterUiState(
    val character: Character? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null
)
