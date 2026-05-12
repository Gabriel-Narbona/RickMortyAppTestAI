package com.example.rickmortyapp.ui.login

sealed interface LoginEffect {
    data object NavigateToSeries : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
