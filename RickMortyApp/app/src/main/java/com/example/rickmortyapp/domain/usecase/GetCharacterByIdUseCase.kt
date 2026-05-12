package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Result<Character> =
        repository.getCharacterById(id)
}
