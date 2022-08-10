package com.softyorch.taskapp.data.preferences

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.taskapp.model.Preferences

@Database(entities = [Preferences::class], version = 1, exportSchema = false)
abstract class PreferencesDataBase: RoomDatabase() {
    abstract fun preferencesDao(): PreferencesDataBaseDao
}