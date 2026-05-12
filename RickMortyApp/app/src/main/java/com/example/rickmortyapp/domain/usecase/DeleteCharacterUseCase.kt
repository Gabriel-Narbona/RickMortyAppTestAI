package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

class DeleteCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteCharacter(id)
    }
}