package com.example.rickmortyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharactersResponseDto(
    val info: ApiInfoDto,
    val results: List<CharacterDto>
)

data class LocationsResponseDto(
    val info: ApiInfoDto,
    val results: List<LocationDto>
)

data class EpisodesResponseDto(
    val info: ApiInfoDto,
    val results: List<EpisodeDto>
)
