package com.example.rickmortyapp.data.repository

import com.example.rickmortyapp.data.remote.api.RickMortyApi
import com.example.rickmortyapp.data.remote.mapper.toDomain
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.repository.EpisodeRepository
import javax.inject.Inject

import com.example.rickmortyapp.domain.model.EpisodeFilter

class EpisodeRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : EpisodeRepository {

    override suspend fun getEpisodes(page: Int, filter: EpisodeFilter?): Result<PagedResult<Episode>> =
        runCatching {
            api.getEpisodes(
                page = page,
                name = filter?.name?.takeIf { it.isNotEmpty() },
                episode = filter?.episode?.takeIf { it.isNotEmpty() }
            ).toDomain()
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun getEpisodeById(id: Int): Result<Episode> =
        runCatching {
            api.getEpisodeById(id).toDomain()
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )
}
