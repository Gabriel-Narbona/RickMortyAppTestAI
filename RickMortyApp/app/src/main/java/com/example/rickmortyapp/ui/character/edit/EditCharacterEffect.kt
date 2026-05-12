package com.example.rickmortyapp.ui.character.edit

sealed interface EditCharacterEffect {
    data object NavigateBack : EditCharacterEffect
    data class ShowMessage(val message: String) : EditCharacterEffect
}
