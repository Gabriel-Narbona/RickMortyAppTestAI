package com.example.rickmortyapp.ui.splash

sealed interface SplashUiEvent {
    data object OnTimeout : SplashUiEvent
}
