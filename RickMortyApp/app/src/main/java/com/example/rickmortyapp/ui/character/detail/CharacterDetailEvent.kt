package com.example.rickmortyapp.ui.character.detail

sealed interface CharacterDetailUiEvent {
    data class LoadCharacter(val id: Int) : CharacterDetailUiEvent
    data object OnDeleteClicked : CharacterDetailUiEvent
    data object OnBackClicked : CharacterDetailUiEvent
}
