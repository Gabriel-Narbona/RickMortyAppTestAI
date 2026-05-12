package com.example.rickmortyapp.data.repository

import com.example.rickmortyapp.data.local.dao.CharacterDao
import com.example.rickmortyapp.data.local.entity.toDomain
import com.example.rickmortyapp.data.local.entity.toEntity
import com.example.rickmortyapp.data.remote.api.RickMortyApi
import com.example.rickmortyapp.data.remote.mapper.toDomain
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

import com.example.rickmortyapp.domain.model.CharacterFilter

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(page: Int, filter: CharacterFilter?): Result<PagedResult<Character>> =
        runCatching {
            val apiResult = api.getCharacters(
                page = page,
                name = filter?.name?.takeIf { it.isNotEmpty() },
                status = filter?.status?.takeIf { it.isNotEmpty() },
                species = filter?.species?.takeIf { it.isNotEmpty() },
                type = filter?.type?.takeIf { it.isNotEmpty() },
                gender = filter?.gender?.takeIf { it.isNotEmpty() }
            ).toDomain()
            
            val modifiedCharacters = dao.getAllModifiedCharacters().associateBy { it.id }
            
            // Merge API data with local modifications and filter out deleted ones
            val mergedCharacters = apiResult.results.mapNotNull { apiChar ->
                val localChar = modifiedCharacters[apiChar.id]
                if (localChar?.isDeleted == true) {
                    null // Skip this character if it's marked as deleted locally
                } else {
                    localChar?.toDomain() ?: apiChar
                }
            }
            
            apiResult.copy(results = mergedCharacters)
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun getCharacterById(id: Int): Result<Character> =
        runCatching {
            // Check local DB first
            val localChar = dao.getCharacterById(id)
            if (localChar != null) {
                if (localChar.isDeleted) {
                    throw Exception("Character not found (deleted)")
                }
                localChar.toDomain()
            } else {
                api.getCharacterById(id).toDomain()
            }
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun getCharactersByIds(ids: List<Int>): Result<List<Character>> =
        runCatching {
            val apiCharacters = api.getCharactersByIds(ids.joinToString(",")).map { it.toDomain() }
            val modifiedCharacters = dao.getAllModifiedCharacters().associateBy { it.id }
            
            apiCharacters.mapNotNull { apiChar ->
                val localChar = modifiedCharacters[apiChar.id]
                if (localChar?.isDeleted == true) {
                    null
                } else {
                    localChar?.toDomain() ?: apiChar
                }
            }
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun saveCharacter(character: Character): Result<Unit> =
        runCatching {
            dao.insertCharacter(character.toEntity())
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun deleteCharacter(id: Int): Result<Unit> =
        runCatching {
            // 1. Check if it exists locally
            val localChar = dao.getCharacterById(id)
            
            val charToSave = if (localChar != null) {
                localChar.copy(isDeleted = true)
            } else {
                // 2. Fetch from API to get data, then save as deleted
                val apiChar = api.getCharacterById(id).toDomain()
                apiChar.toEntity(isDeleted = true)
            }
            
            dao.insertCharacter(charToSave)
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(it) }
        )
}