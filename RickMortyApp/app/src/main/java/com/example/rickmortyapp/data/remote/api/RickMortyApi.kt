package com.example.rickmortyapp.data.remote.api

import com.example.rickmortyapp.data.remote.dto.CharacterDto
import com.example.rickmortyapp.data.remote.dto.CharactersResponseDto
import com.example.rickmortyapp.data.remote.dto.EpisodeDto
import com.example.rickmortyapp.data.remote.dto.EpisodesResponseDto
import com.example.rickmortyapp.data.remote.dto.LocationDto
import com.example.rickmortyapp.data.remote.dto.LocationsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): CharactersResponseDto

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto

    @GET("character/{ids}")
    suspend fun getCharactersByIds(@Path("ids") ids: String): List<CharacterDto>

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): LocationsResponseDto

    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationDto

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null
    ): EpisodesResponseDto

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeDto
}
