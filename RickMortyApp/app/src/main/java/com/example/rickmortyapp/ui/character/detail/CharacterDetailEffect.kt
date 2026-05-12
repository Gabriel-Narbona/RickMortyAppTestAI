package com.example.rickmortyapp.ui.character.detail

sealed interface CharacterDetailEffect {
    data object NavigateBack : CharacterDetailEffect
}
