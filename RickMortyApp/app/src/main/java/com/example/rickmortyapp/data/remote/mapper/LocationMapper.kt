package com.example.rickmortyapp.data.remote.mapper

import com.example.rickmortyapp.data.remote.dto.ApiInfoDto
import com.example.rickmortyapp.data.remote.dto.LocationDto
import com.example.rickmortyapp.data.remote.dto.LocationsResponseDto
import com.example.rickmortyapp.domain.model.Location
import com.example.rickmortyapp.domain.model.PageInfo
import com.example.rickmortyapp.domain.model.PagedResult

fun LocationDto.toDomain(): Location =
    Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residentUrls = residents,
        url = url,
        created = created
    )

fun LocationsResponseDto.toDomain(): PagedResult<Location> =
    PagedResult(
        info = info.toDomain(),
        results = results.map { it.toDomain() }
    )
