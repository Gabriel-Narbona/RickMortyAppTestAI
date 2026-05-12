package com.example.rickmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.example.rickmortyapp.ui.navigation.RickMortyNavHost
import com.example.rickmortyapp.ui.theme.RickMortyAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickMortyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RickMortyNavHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
