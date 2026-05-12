package com.example.rickmortyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickmortyapp.domain.model.Character

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originUrl: String?,
    val locationName: String,
    val locationUrl: String?,
    val imageUrl: String,
    val episodeUrls: String, // Stored as comma-separated string
    val url: String,
    val created: String,
    val isDeleted: Boolean = false
)

fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    originName = originName,
    originUrl = originUrl,
    locationName = locationName,
    locationUrl = locationUrl,
    imageUrl = imageUrl,
    episodeUrls = episodeUrls.split(",").filter { it.isNotEmpty() },
    url = url,
    created = created
)

fun Character.toEntity(isDeleted: Boolean = false): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    originName = originName,
    originUrl = originUrl,
    locationName = locationName,
    locationUrl = locationUrl,
    imageUrl = imageUrl,
    episodeUrls = episodeUrls.joinToString(","),
    url = url,
    created = created,
    isDeleted = isDeleted
)