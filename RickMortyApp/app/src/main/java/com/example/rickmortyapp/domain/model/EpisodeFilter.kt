package com.example.rickmortyapp.domain.model

data class EpisodeFilter(
    val name: String = "",
    val episode: String = ""
) {
    fun isActive(): Boolean = name.isNotEmpty() || episode.isNotEmpty()
}
