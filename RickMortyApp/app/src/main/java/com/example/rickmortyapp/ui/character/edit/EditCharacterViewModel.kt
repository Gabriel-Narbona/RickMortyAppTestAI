package com.example.rickmortyapp.ui.character.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.usecase.GetCharacterByIdUseCase
import com.example.rickmortyapp.domain.usecase.SaveCharacterUseCase
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
class EditCharacterViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val saveCharacterUseCase: SaveCharacterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditCharacterUiState())
    val state: StateFlow<EditCharacterUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<EditCharacterEffect>()
    val effects = _effects.asSharedFlow()

    fun handleEvent(event: EditCharacterUiEvent) {
        when (event) {
            is EditCharacterUiEvent.LoadCharacter -> loadCharacter(event.id)
            is EditCharacterUiEvent.UpdateName -> _state.update { 
                it.copy(character = it.character?.copy(name = event.name)) 
            }
            is EditCharacterUiEvent.UpdateStatus -> _state.update { 
                it.copy(character = it.character?.copy(status = event.status)) 
            }
            is EditCharacterUiEvent.UpdateSpecies -> _state.update { 
                it.copy(character = it.character?.copy(species = event.species)) 
            }
            is EditCharacterUiEvent.UpdateType -> _state.update { 
                it.copy(character = it.character?.copy(type = event.type)) 
            }
            is EditCharacterUiEvent.UpdateGender -> _state.update { 
                it.copy(character = it.character?.copy(gender = event.gender)) 
            }
            is EditCharacterUiEvent.SaveCharacter -> saveCharacter()
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

    private fun saveCharacter() {
        val character = _state.value.character ?: return
        
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            when (val result = saveCharacterUseCase(character)) {
                is Result.Success -> {
                    _state.update { it.copy(isSaving = false) }
                    _effects.emit(EditCharacterEffect.ShowMessage("Personaje guardado correctamente"))
                    _effects.emit(EditCharacterEffect.NavigateBack)
                }
                is Result.Error -> {
                    _state.update { it.copy(isSaving = false) }
                    _effects.emit(EditCharacterEffect.ShowMessage("Error al guardar: ${result.exception.message}"))
                }
            }
        }
    }
}
