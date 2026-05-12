package com.example.rickmortyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>(replay = 0, extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> _state.update {
                it.copy(username = event.value, errorMessage = null)
            }
            is LoginUiEvent.PasswordChanged -> _state.update {
                it.copy(password = event.value, errorMessage = null)
            }
            is LoginUiEvent.LoginClicked -> tryLogin()
        }
    }

    private fun tryLogin() {
        val current = _state.value
        when {
            current.username.isBlank() -> viewModelScope.launch {
                _effects.emit(LoginEffect.ShowError("Introduce tu usuario"))
            }
            current.password.isBlank() -> viewModelScope.launch {
                _effects.emit(LoginEffect.ShowError("Introduce tu contraseña"))
            }
            else -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                _state.update { it.copy(isLoading = false) }
                _effects.emit(LoginEffect.NavigateToSeries)
            }
        }
    }
}
