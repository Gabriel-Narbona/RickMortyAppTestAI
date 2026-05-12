package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodeByIdUseCase @Inject constructor(
    private val repository: EpisodeRepository
) {
    suspend operator fun invoke(id: Int): Result<Episode> =
        repository.getEpisodeById(id)
}
