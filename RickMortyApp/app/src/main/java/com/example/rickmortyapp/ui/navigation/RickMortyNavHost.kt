package com.example.rickmortyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.rickmortyapp.ui.login.LoginScreen
import com.example.rickmortyapp.ui.series.SeriesScreen
import com.example.rickmortyapp.ui.splash.SplashScreen

import com.example.rickmortyapp.ui.character.detail.CharacterDetailScreen
import com.example.rickmortyapp.ui.character.edit.EditCharacterScreen

@Composable
fun RickMortyNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf<NavKey>(NavKey.Splash)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider<NavKey> {
            entry<NavKey.Splash> {
                SplashScreen(
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(NavKey.Login)
                    }
                )
            }
            entry<NavKey.Login> {
                LoginScreen(
                    onNavigateToSeries = {
                        backStack.clear()
                        backStack.add(NavKey.Series)
                    }
                )
            }
            entry<NavKey.Series> {
                SeriesScreen(
                    onCharacterClick = { id -> backStack.add(NavKey.CharacterDetail(id)) }
                )
            }

            entry<NavKey.CharacterDetail> { key ->
                CharacterDetailScreen(
                    characterId = key.id,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onEditClick = { id -> backStack.add(NavKey.EditCharacter(id)) }
                )
            }
            entry<NavKey.EditCharacter> { key ->
                EditCharacterScreen(
                    characterId = key.id,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}