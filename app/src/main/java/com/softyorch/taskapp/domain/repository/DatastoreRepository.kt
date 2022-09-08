package com.softyorch.taskapp.domain.repository

import com.softyorch.taskapp.data.data.datastore.DatastoreDataBase
import com.softyorch.taskapp.domain.model.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepository @Inject constructor(
    private val repository: DatastoreDataBase
) {
    fun getData() = repository.getData()
    suspend fun saveData(userData: UserData) = repository.saveData(userData = userData)
    suspend fun deleteData() = repository.deleteData()
}