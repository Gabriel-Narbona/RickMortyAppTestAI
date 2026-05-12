package com.example.rickmortyapp.ui.splash

sealed interface SplashEffect {
    data object NavigateToLogin : SplashEffect
}
