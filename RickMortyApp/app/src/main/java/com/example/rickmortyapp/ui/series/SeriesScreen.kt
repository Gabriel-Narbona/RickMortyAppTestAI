package com.example.rickmortyapp.ui.series

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rickmortyapp.ui.theme.RickMortyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesScreen(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effects
    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleResumeEffect(Unit) {
        // Refresh data when screen resumes (e.g. returning from Detail/Delete)
        if (state.value.selectedTab == HomeTab.Personajes) {
            viewModel.handleEvent(SeriesUiEvent.OnRefresh)
        }
        onPauseOrDispose { }
    }

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is SeriesEffect.ShowToast -> snackbarHostState.showSnackbar(effect.message)
                is SeriesEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val topBarTitle = when (state.value.selectedTab) {
        HomeTab.Personajes -> "Personajes"
        HomeTab.Capítulos -> "Capítulos"
    }

    if (state.value.isFilterVisible) {
        FilterBottomSheet(
            selectedTab = state.value.selectedTab,
            characterFilter = state.value.characterFilter,
            episodeFilter = state.value.episodeFilter,
            onUpdateCharacterFilter = { viewModel.handleEvent(SeriesUiEvent.UpdateCharacterFilter(it)) },
            onUpdateEpisodeFilter = { viewModel.handleEvent(SeriesUiEvent.UpdateEpisodeFilter(it)) },
            onApply = { viewModel.handleEvent(SeriesUiEvent.ApplyFilter) },
            onClear = { viewModel.handleEvent(SeriesUiEvent.ClearFilter) },
            onDismiss = { viewModel.handleEvent(SeriesUiEvent.ToggleFilterVisibility) }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.handleEvent(SeriesUiEvent.ToggleFilterVisibility) }) {
                        val isActive = if (state.value.selectedTab == HomeTab.Personajes) {
                            state.value.characterFilter.isActive()
                        } else {
                            state.value.episodeFilter.isActive()
                        }
                        
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = state.value.selectedTab == HomeTab.Personajes,
                    onClick = { viewModel.handleEvent(SeriesUiEvent.SelectTab(HomeTab.Personajes)) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Personajes") },
                    label = { Text("Personajes") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = state.value.selectedTab == HomeTab.Capítulos,
                    onClick = { viewModel.handleEvent(SeriesUiEvent.SelectTab(HomeTab.Capítulos)) },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Capítulos") },
                    label = { Text("Capítulos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        SeriesContent(
            state = state.value,
            onEvent = viewModel::handleEvent,
            onCharacterClick = onCharacterClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SeriesContent(
    state: SeriesUiState,
    onEvent: (SeriesUiEvent) -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.selectedTab) {
        HomeTab.Personajes -> CharactersContent(
            state = state,
            onEvent = onEvent,
            onCharacterClick = onCharacterClick,
            modifier = modifier
        )

        HomeTab.Capítulos -> EpisodesContent(state = state, onEvent = onEvent, modifier = modifier)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CharactersContent(
    state: SeriesUiState,
    onEvent: (SeriesUiEvent) -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisible >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && state.charactersHasNextPage && !state.charactersLoadingMore) {
            onEvent(SeriesUiEvent.LoadNextPage)
        }
    }

    PullToRefreshBox(
        isRefreshing = state.charactersRefreshing,
        onRefresh = { onEvent(SeriesUiEvent.OnRefresh) },
        modifier = modifier
    ) {
        when {
            state.charactersLoading && state.characters.isEmpty() -> LoadingContent(modifier = Modifier.fillMaxSize())
            state.charactersError != null && state.characters.isEmpty() -> ErrorContent(
                message = state.charactersError,
                onRetry = { onEvent(SeriesUiEvent.OnRetry) },
                modifier = Modifier.fillMaxSize()
            )

            state.characters.isEmpty() -> EmptyContent(
                message = "No hay personajes",
                modifier = Modifier.fillMaxSize()
            )

            else -> LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "header", span = { GridItemSpan(2) }) {
                    Text(
                        text = "${state.charactersTotalCount} personajes · Página ${state.charactersCurrentPage}/${state.charactersTotalPages}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(
                    items = state.characters,
                    key = { it.id }
                ) { character ->
                    CharacterCard(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.charactersLoadingMore) {
                    item(key = "loading_more", span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EpisodesContent(
    state: SeriesUiState,
    onEvent: (SeriesUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisible >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && state.hasNextPage && !state.isLoadingMore) {
            onEvent(SeriesUiEvent.LoadNextPage)
        }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onEvent(SeriesUiEvent.OnRefresh) },
        modifier = modifier
    ) {
        when {
            state.isLoading && state.episodes.isEmpty() -> LoadingContent(modifier = Modifier.fillMaxSize())
            state.error != null && state.episodes.isEmpty() -> ErrorContent(
                message = state.error,
                onRetry = { onEvent(SeriesUiEvent.OnRetry) },
                modifier = Modifier.fillMaxSize()
            )

            state.episodes.isEmpty() -> EmptyContent(
                message = "No hay episodios",
                modifier = Modifier.fillMaxSize()
            )

            else -> LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "header") {
                    Text(
                        text = "${state.totalCount} episodios · Página ${state.currentPage}/${state.totalPages}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(
                    items = state.episodes,
                    key = { it.id }
                ) { episode ->
                    EpisodeCard(episode = episode)
                }
                if (state.isLoadingMore) {
                    item(key = "loading_more") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun EmptyContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SeriesScreenPreview() {
    RickMortyAppTheme {
        SeriesScreen({})
    }
}
