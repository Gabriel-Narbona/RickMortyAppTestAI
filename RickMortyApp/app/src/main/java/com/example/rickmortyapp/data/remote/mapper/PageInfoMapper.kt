package com.example.rickmortyapp.data.remote.mapper

import com.example.rickmortyapp.data.remote.dto.ApiInfoDto
import com.example.rickmortyapp.domain.model.PageInfo

fun ApiInfoDto.toDomain(): PageInfo =
    PageInfo(
        count = count,
        pages = pages,
        next = next,
        prev = prev
    )
