package com.softyorch.taskapp.repository

import com.softyorch.taskapp.data.settings.SettingsDataBaseDao
import com.softyorch.taskapp.model.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsDataBaseDao: SettingsDataBaseDao
) {
    fun getAllSettings(): Flow<List<Settings>> =
        settingsDataBaseDao.getSettings().flowOn(Dispatchers.IO).conflate()

    suspend fun insertSettings(settings: Settings) =
        settingsDataBaseDao.insert(settings = settings)

    suspend fun updateSettings(settings: Settings) =
        settingsDataBaseDao.update(settings = settings)
}