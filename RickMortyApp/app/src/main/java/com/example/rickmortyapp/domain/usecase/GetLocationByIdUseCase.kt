package com.example.rickmortyapp.domain.usecase

import com.example.rickmortyapp.domain.Result
import com.example.rickmortyapp.domain.model.Location
import com.example.rickmortyapp.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(id: Int): Result<Location> =
        repository.getLocationById(id)
}
