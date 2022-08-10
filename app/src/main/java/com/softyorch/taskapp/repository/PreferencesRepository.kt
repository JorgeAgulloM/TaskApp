package com.softyorch.taskapp.repository

import com.softyorch.taskapp.data.preferences.PreferencesDataBaseDao
import com.softyorch.taskapp.model.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferencesDataBaseDao: PreferencesDataBaseDao
) {
    fun getAllPreferences(): Flow<List<Preferences>> =
        preferencesDataBaseDao.getPreferences().flowOn(Dispatchers.IO).conflate()

    suspend fun insertPreferences(preferences: Preferences) =
        preferencesDataBaseDao.insert(preferences = preferences)

    suspend fun updatePreferences(preferences: Preferences) =
        preferencesDataBaseDao.update(preferences = preferences)
}