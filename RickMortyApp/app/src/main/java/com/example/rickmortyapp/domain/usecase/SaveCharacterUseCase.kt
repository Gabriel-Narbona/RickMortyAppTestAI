package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

class SaveCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(character: Character): Result<Unit> =
        repository.saveCharacter(character)
}
