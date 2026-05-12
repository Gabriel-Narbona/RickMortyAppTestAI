package com.example.rickmortyapp.ui.character.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.usecase.GetCharacterByIdUseCase
import com.example.rickmortyapp.domain.usecase.DeleteCharacterUseCase
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
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailUiState())
    val state: StateFlow<CharacterDetailUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CharacterDetailEffect>()
    val effects = _effects.asSharedFlow()

    fun handleEvent(event: CharacterDetailUiEvent) {
        when (event) {
            is CharacterDetailUiEvent.LoadCharacter -> loadCharacter(event.id)
            is CharacterDetailUiEvent.OnBackClicked -> viewModelScope.launch {
                _effects.emit(CharacterDetailEffect.NavigateBack)
            }
            is CharacterDetailUiEvent.OnDeleteClicked -> deleteCharacter()
        }
    }

    private fun deleteCharacter() {
        val currentCharacter = _state.value.character ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = deleteCharacterUseCase(currentCharacter.id)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(CharacterDetailEffect.NavigateBack)
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                }
            }
        }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getCharacterByIdUseCase(id)) {
                is Result.Success -> _state.update {
                    it.copy(character = result.data, isLoading = false)
                }
                is Result.Error -> _state.update {
                    it.copy(isLoading = false, error = result.exception.message)
                }
            }
        }
    }
}
