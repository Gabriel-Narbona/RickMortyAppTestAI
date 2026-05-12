package com.example.rickmortyapp.domain.repository

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Location
import com.example.rickmortyapp.domain.model.PagedResult

interface LocationRepository {

    suspend fun getLocations(page: Int = 1): Result<PagedResult<Location>>

    suspend fun getLocationById(id: Int): Result<Location>
}
