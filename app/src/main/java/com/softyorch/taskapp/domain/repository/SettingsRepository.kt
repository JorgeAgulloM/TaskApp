package com.softyorch.taskapp.domain.repository

import com.softyorch.taskapp.data.data.datastore.DatastoreDataBase
import com.softyorch.taskapp.domain.model.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val repository: DatastoreDataBase
) {
    suspend fun saveData(userData: UserData) = repository.savaData(userData = userData)
    suspend fun deleteData() = repository.deleteData()
    fun getData() = repository.getData()
}