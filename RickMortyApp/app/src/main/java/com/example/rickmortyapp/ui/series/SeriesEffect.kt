package com.example.rickmortyapp.ui.series

sealed interface SeriesEffect {
    data class ShowToast(val message: String) : SeriesEffect
    data class ShowError(val message: String) : SeriesEffect
}
