package com.softyorch.taskapp.data.di

import android.content.Context
import com.softyorch.taskapp.data.data.datastore.DatastoreDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SettingsModule {

    @Singleton
    @Provides
    fun providesDatastoreDataBase(@ApplicationContext context: Context): DatastoreDataBase =
        DatastoreDataBase(context = context)
}