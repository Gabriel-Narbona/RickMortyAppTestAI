package com.example.rickmortyapp.data.repository

import com.example.rickmortyapp.data.remote.api.RickMortyApi
import com.example.rickmortyapp.data.remote.mapper.toDomain
import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Location
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : LocationRepository {

    override suspend fun getLocations(page: Int): Result<PagedResult<Location>> =
        runCatching {
            api.getLocations(page).toDomain()
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )

    override suspend fun getLocationById(id: Int): Result<Location> =
        runCatching {
            api.getLocationById(id).toDomain()
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error(it) }
        )
}
