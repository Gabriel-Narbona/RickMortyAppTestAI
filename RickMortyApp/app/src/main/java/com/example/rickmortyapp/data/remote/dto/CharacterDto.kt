package com.example.rickmortyapp.data.remote.dto

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginLocationDto,
    val location: OriginLocationDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class OriginLocationDto(
    val name: String,
    val url: String
)
