package com.softyorch.taskapp.data.settings

import androidx.room.*
import com.softyorch.taskapp.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDataBaseDao {

    @Query("SELECT * FROM settings_tbl")
    fun getSettings(): Flow<List<Settings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(settings: Settings)
}