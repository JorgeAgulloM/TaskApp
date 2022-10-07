package com.softyorch.taskapp.di

import com.softyorch.taskapp.data.repository.pexels.PexelsRepository
import com.softyorch.taskapp.domain.pexelUseCase.GetImage
import com.softyorch.taskapp.domain.pexelUseCase.PexelsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit():Retrofit = Retrofit.Builder()
        .baseUrl("https://api.pexels.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesPexelsRepositoryToUseCases(repository: PexelsRepository): PexelsUseCases =
        PexelsUseCases(
            getImage = GetImage(repository)
        )
}