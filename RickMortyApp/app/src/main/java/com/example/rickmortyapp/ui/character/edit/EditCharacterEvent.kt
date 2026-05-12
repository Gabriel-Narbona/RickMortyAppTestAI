package com.example.rickmortyapp.ui.character.edit

sealed interface EditCharacterUiEvent {
    data class LoadCharacter(val id: Int) : EditCharacterUiEvent
    data class UpdateName(val name: String) : EditCharacterUiEvent
    data class UpdateStatus(val status: String) : EditCharacterUiEvent
    data class UpdateSpecies(val species: String) : EditCharacterUiEvent
    data class UpdateType(val type: String) : EditCharacterUiEvent
    data class UpdateGender(val gender: String) : EditCharacterUiEvent
    data object SaveCharacter : EditCharacterUiEvent
}
