package com.example.rickmortyapp.ui.login

sealed interface LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data object LoginClicked : LoginUiEvent
}
