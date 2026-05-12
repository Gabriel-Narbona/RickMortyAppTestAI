package com.example.rickmortyapp.ui.series

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickmortyapp.domain.model.CharacterFilter
import com.example.rickmortyapp.domain.model.EpisodeFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    selectedTab: HomeTab,
    characterFilter: CharacterFilter,
    episodeFilter: EpisodeFilter,
    onUpdateCharacterFilter: (CharacterFilter) -> Unit,
    onUpdateEpisodeFilter: (EpisodeFilter) -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (selectedTab == HomeTab.Personajes) "Filtrar Personajes" else "Filtrar Capítulos",
                style = MaterialTheme.typography.titleLarge
            )

            if (selectedTab == HomeTab.Personajes) {
                CharacterFilters(filter = characterFilter, onUpdateFilter = onUpdateCharacterFilter)
            } else {
                EpisodeFilters(filter = episodeFilter, onUpdateFilter = onUpdateEpisodeFilter)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aplicar")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterFilters(
    filter: CharacterFilter,
    onUpdateFilter: (CharacterFilter) -> Unit
) {
    OutlinedTextField(
        value = filter.name,
        onValueChange = { onUpdateFilter(filter.copy(name = it)) },
        label = { Text("Nombre") },
        modifier = Modifier.fillMaxWidth()
    )

    Text(text = "Estado", style = MaterialTheme.typography.titleMedium)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("alive", "dead", "unknown").forEach { status ->
            FilterChip(
                selected = filter.status.equals(status, ignoreCase = true),
                onClick = { 
                    onUpdateFilter(filter.copy(status = if (filter.status.equals(status, ignoreCase = true)) "" else status)) 
                },
                label = { Text(status.replaceFirstChar { it.uppercase() }) }
            )
        }
    }

    OutlinedTextField(
        value = filter.species,
        onValueChange = { onUpdateFilter(filter.copy(species = it)) },
        label = { Text("Especie") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = filter.type,
        onValueChange = { onUpdateFilter(filter.copy(type = it)) },
        label = { Text("Tipo") },
        modifier = Modifier.fillMaxWidth()
    )

    Text(text = "Género", style = MaterialTheme.typography.titleMedium)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("female", "male", "genderless", "unknown").forEach { gender ->
            FilterChip(
                selected = filter.gender.equals(gender, ignoreCase = true),
                onClick = { 
                    onUpdateFilter(filter.copy(gender = if (filter.gender.equals(gender, ignoreCase = true)) "" else gender)) 
                },
                label = { Text(gender.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
private fun EpisodeFilters(
    filter: EpisodeFilter,
    onUpdateFilter: (EpisodeFilter) -> Unit
) {
    OutlinedTextField(
        value = filter.name,
        onValueChange = { onUpdateFilter(filter.copy(name = it)) },
        label = { Text("Nombre del episodio") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = filter.episode,
        onValueChange = { onUpdateFilter(filter.copy(episode = it)) },
        label = { Text("Código (ej. S01E01)") },
        modifier = Modifier.fillMaxWidth()
    )
}
