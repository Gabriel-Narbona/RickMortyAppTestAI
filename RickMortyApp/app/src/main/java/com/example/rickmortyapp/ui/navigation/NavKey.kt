package com.example.rickmortyapp.ui.navigation

import java.io.Serializable
import androidx.navigation3.runtime.NavKey as NavKeyRuntime

sealed interface NavKey : NavKeyRuntime, Serializable {
    data object Splash : NavKey
    data object Login : NavKey
    data object Series : NavKey
    data class CharacterDetail(val id: Int) : NavKey
    data class EditCharacter(val id: Int) : NavKey
}