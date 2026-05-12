package com.example.rickmortyapp.domain.repository

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Episode
import com.example.rickmortyapp.domain.model.PagedResult

import com.example.rickmortyapp.domain.model.EpisodeFilter

interface EpisodeRepository {

    suspend fun getEpisodes(page: Int = 1, filter: EpisodeFilter? = null): Result<PagedResult<Episode>>

    suspend fun getEpisodeById(id: Int): Result<Episode>
}
