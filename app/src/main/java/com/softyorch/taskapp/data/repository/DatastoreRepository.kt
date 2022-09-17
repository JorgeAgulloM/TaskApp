package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.datastore.DatastoreDataBase
import com.softyorch.taskapp.data.database.userdata.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepository @Inject constructor(
    private val repository: DatastoreDataBase
) {
    fun getData(): Resource<Flow<UserData>> = repository.getData()
    suspend fun saveData(userData: UserData) = repository.saveData(userData = userData)
    suspend fun deleteData() = repository.deleteData()
}