package com.softyorch.taskapp.di

import android.content.Context
import androidx.room.Room
import com.softyorch.taskapp.data.TaskDatabase
import com.softyorch.taskapp.data.TaskDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDatabaseDao = taskDatabase.taskDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "tasks_tbl")
            .fallbackToDestructiveMigration()
            .build()
}