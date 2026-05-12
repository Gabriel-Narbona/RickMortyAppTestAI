package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.repository.EpisodeRepository
import javax.inject.Inject

import com.example.rickmortyapp.domain.model.EpisodeFilter

class GetEpisodesUseCase @Inject constructor(
    private val repository: EpisodeRepository
) {
    suspend operator fun invoke(page: Int = 1, filter: EpisodeFilter? = null): Result<PagedResult<Episode>> =
        repository.getEpisodes(page, filter)
}
