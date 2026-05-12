package com.example.rickmortyapp.data.remote.mapper

import com.example.rickmortyapp.data.remote.dto.ApiInfoDto
import com.example.rickmortyapp.data.remote.dto.EpisodeDto
import com.example.rickmortyapp.data.remote.dto.EpisodesResponseDto
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.model.PageInfo
import com.example.rickmortyapp.domain.model.PagedResult

fun EpisodeDto.toDomain(): Episode =
    Episode(
        id = id,
        name = name,
        airDate = airDate,
        episodeCode = episode,
        characterUrls = characters,
        url = url,
        created = created
    )

fun EpisodesResponseDto.toDomain(): PagedResult<Episode> =
    PagedResult(
        info = info.toDomain(),
        results = results.map { it.toDomain() }
    )
