package com.example.rickmortyapp.domain.repository

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.model.PagedResult

import com.example.rickmortyapp.domain.model.CharacterFilter

interface CharacterRepository {

    suspend fun getCharacters(page: Int = 1, filter: CharacterFilter? = null): Result<PagedResult<Character>>

    suspend fun getCharacterById(id: Int): Result<Character>

    suspend fun getCharactersByIds(ids: List<Int>): Result<List<Character>>

    suspend fun saveCharacter(character: Character): Result<Unit>

    suspend fun deleteCharacter(id: Int): Result<Unit>
}
