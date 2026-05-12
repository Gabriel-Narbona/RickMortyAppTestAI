package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

import com.example.rickmortyapp.domain.model.CharacterFilter

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int = 1, filter: CharacterFilter? = null): Result<PagedResult<Character>> =
        repository.getCharacters(page, filter)
}
