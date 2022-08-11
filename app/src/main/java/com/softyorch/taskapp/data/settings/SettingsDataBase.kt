package com.softyorch.taskapp.data.settings

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.taskapp.model.Settings

@Database(entities = [Settings::class], version = 2, exportSchema = false)
abstract class SettingsDataBase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDataBaseDao
}