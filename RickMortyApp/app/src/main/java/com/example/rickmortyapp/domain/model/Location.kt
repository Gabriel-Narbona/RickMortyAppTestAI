package com.example.rickmortyapp.domain.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentUrls: List<String>,
    val url: String,
    val created: String
)
