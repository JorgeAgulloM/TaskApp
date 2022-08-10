package com.softyorch.taskapp.data.preferences

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.softyorch.taskapp.model.Preferences
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDataBaseDao {

    @Query("SELECT * FROM preferences_tbl")
    fun getPreferences(): Flow<List<Preferences>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(preferences: Preferences)
}