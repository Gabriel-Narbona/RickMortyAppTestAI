package com.example.rickmortyapp.di

import com.example.rickmortyapp.data.repository.CharacterRepositoryImpl
import com.example.rickmortyapp.data.repository.EpisodeRepositoryImpl
import com.example.rickmortyapp.data.repository.LocationRepositoryImpl
import com.example.rickmortyapp.domain.repository.CharacterRepository
import com.example.rickmortyapp.domain.repository.EpisodeRepository
import com.example.rickmortyapp.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository
}
