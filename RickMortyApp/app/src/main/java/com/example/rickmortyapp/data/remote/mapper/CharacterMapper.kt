package com.example.rickmortyapp.data.remote.mapper

import com.example.rickmortyapp.data.remote.dto.CharacterDto
import com.example.rickmortyapp.data.remote.dto.CharactersResponseDto
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.domain.model.PagedResult

fun CharacterDto.toDomain(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type.ifEmpty() { "" },
        gender = gender,
        originName = origin.name,
        originUrl = origin.url.takeIf { it.isNotEmpty() },
        locationName = location.name,
        locationUrl = location.url.takeIf { it.isNotEmpty() },
        imageUrl = image,
        episodeUrls = episode,
        url = url,
        created = created
    )

fun CharactersResponseDto.toDomain(): PagedResult<Character> =
    PagedResult(
        info = info.toDomain(),
        results = results.map(CharacterDto::toDomain)
    )
