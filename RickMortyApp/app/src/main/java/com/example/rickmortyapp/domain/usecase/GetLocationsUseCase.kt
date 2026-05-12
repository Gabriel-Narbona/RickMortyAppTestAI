package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Location
import com.example.rickmortyapp.domain.model.PagedResult
import com.example.rickmortyapp.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<PagedResult<Location>> =
        repository.getLocations(page)
}
