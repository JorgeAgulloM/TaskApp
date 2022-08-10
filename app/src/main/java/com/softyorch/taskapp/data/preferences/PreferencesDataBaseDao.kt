package com.softyorch.taskapp.data.preferences

import androidx.room.*
import com.softyorch.taskapp.model.Preferences
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDataBaseDao {

    @Query("SELECT * FROM preferences_tbl")
    fun getPreferences(): Flow<List<Preferences>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: Preferences)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(preferences: Preferences)
}