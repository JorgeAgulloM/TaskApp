package com.softyorch.taskapp.di

import android.content.Context
import androidx.room.Room
import com.softyorch.taskapp.data.userdata.UserDataBase
import com.softyorch.taskapp.data.userdata.UserDataBaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserDataModule {

    @Singleton
    @Provides
    fun providesUserData(userDataBase: UserDataBase): UserDataBaseDao = userDataBase.userDataDao()

    @Singleton
    @Provides
    fun providesUserDataBase(@ApplicationContext context: Context): UserDataBase =
        Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            "userdata_tbl")
            .fallbackToDestructiveMigration()
            .build()
}