package com.example.rickmortyapp.domain.model

data class PagedResult<T>(
    val info: PageInfo,
    val results: List<T>
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
