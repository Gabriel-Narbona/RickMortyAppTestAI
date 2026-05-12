package com.example.rickmortyapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DELAY_MS = 2_000L

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SplashUiState())
    val state: StateFlow<SplashUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SplashEffect>(replay = 0, extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS)
            _effects.emit(SplashEffect.NavigateToLogin)
        }
    }

    fun handleEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.OnTimeout -> viewModelScope.launch {
                _effects.emit(SplashEffect.NavigateToLogin)
            }
        }
    }
}
