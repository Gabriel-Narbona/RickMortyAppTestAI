package com.example.rickmortyapp.ui.character.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rickmortyapp.domain.model.Character
import com.example.rickmortyapp.ui.theme.RickMortyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacterScreen(
    characterId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditCharacterViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effects
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(characterId) {
        viewModel.handleEvent(EditCharacterUiEvent.LoadCharacter(characterId))
    }

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is EditCharacterEffect.NavigateBack -> onNavigateBack()
                is EditCharacterEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Editar Personaje") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleEvent(EditCharacterUiEvent.SaveCharacter) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.value.error != null) {
                Text(
                    text = state.value.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                state.value.character?.let { character ->
                    EditCharacterContent(
                        character = character,
                        onEvent = viewModel::handleEvent
                    )
                }
            }
            
            if (state.value.isSaving) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun EditCharacterContent(
    character: Character,
    onEvent: (EditCharacterUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = character.name,
            onValueChange = { onEvent(EditCharacterUiEvent.UpdateName(it)) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = character.status,
            onValueChange = { onEvent(EditCharacterUiEvent.UpdateStatus(it)) },
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = character.species,
            onValueChange = { onEvent(EditCharacterUiEvent.UpdateSpecies(it)) },
            label = { Text("Especie") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = character.gender,
            onValueChange = { onEvent(EditCharacterUiEvent.UpdateGender(it)) },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = character.type,
            onValueChange = { onEvent(EditCharacterUiEvent.UpdateType(it)) },
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
