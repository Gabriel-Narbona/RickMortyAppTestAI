package com.example.rickmortyapp.di

import com.example.rickmortyapp.data.remote.api.RickMortyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor { chain ->
                var response = chain.proceed(chain.request())
                var tryCount = 0
                val maxLimit = 3
                
                while (!response.isSuccessful && response.code == 429 && tryCount < maxLimit) {
                    tryCount++
                    // Espera exponencial: 1s, 2s, 4s
                    try {
                        Thread.sleep(1000L * tryCount) 
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    response.close()
                    response = chain.proceed(chain.request())
                }
                response
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRickMortyApi(retrofit: Retrofit): RickMortyApi =
        retrofit.create(RickMortyApi::class.java)
}
