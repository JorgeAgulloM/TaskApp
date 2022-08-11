package com.softyorch.taskapp.di

import android.content.Context
import androidx.room.Room
import com.softyorch.taskapp.data.settings.SettingsDataBase
import com.softyorch.taskapp.data.settings.SettingsDataBaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserSettingsModule {

    @Singleton
    @Provides
    fun provideSettingsDao(settingsDataBase: SettingsDataBase):
            SettingsDataBaseDao = settingsDataBase.settingsDao()

    @Singleton
    @Provides
    fun provideSettingsDataBase(@ApplicationContext context: Context): SettingsDataBase =
        Room.databaseBuilder(
            context,
            SettingsDataBase::class.java,
            "settings_tbl")
            .fallbackToDestructiveMigration()
            .build()
}