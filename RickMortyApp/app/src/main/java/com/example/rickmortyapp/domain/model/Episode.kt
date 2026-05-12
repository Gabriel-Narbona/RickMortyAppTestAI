package com.example.rickmortyapp.domain.model

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episodeCode: String,
    val characterUrls: List<String>,
    val url: String,
    val created: String
)
