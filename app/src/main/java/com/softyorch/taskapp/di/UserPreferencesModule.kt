package com.softyorch.taskapp.di

import android.content.Context
import androidx.room.Room
import com.softyorch.taskapp.data.preferences.PreferencesDataBase
import com.softyorch.taskapp.data.preferences.PreferencesDataBaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserPreferencesModule {

    @Singleton
    @Provides
    fun providePreferencesDao(preferencesDataBase: PreferencesDataBase):
            PreferencesDataBaseDao = preferencesDataBase.preferencesDao()

    @Singleton
    @Provides
    fun providePreferencesDataBase(@ApplicationContext context: Context): PreferencesDataBase =
        Room.databaseBuilder(
            context,
            PreferencesDataBase::class.java,
            "preferences_tbl")
            .fallbackToDestructiveMigration()
            .build()
}