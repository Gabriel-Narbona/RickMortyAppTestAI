package com.example.rickmortyapp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.painterResource
import com.example.rickmortyapp.R
import com.example.rickmortyapp.ui.theme.RickMortyAppTheme

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effects

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is SplashEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    SplashContent(
        state = state.value,
        modifier = modifier
    )
}

@Composable
private fun SplashContent(
    state: SplashUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rick and Morty Logo/Image from local drawable
            Image(
                painter = painterResource(id = R.drawable.rick_and_morty_title_splash_screen),
                contentDescription = "Rick and Morty Logo",
                modifier = Modifier.size(280.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
            }
        }
        
        Text(
            text = "Bienvenido al Multiverso",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashContentPreview() {
    RickMortyAppTheme {
        SplashContent(state = SplashUiState(isLoading = true))
    }
}
