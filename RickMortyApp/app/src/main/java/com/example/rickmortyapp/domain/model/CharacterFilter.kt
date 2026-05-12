package com.example.rickmortyapp.domain.model

data class CharacterFilter(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val type: String = "",
    val gender: String = ""
) {
    fun isActive(): Boolean = name.isNotEmpty() || status.isNotEmpty() || 
            species.isNotEmpty() || type.isNotEmpty() || gender.isNotEmpty()
}
