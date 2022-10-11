package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.database.datastore.DatastoreDataBase
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepository @Inject constructor(
    private val repository: DatastoreDataBase
) {
    fun getData(): Flow<UserDataEntity>? = repository.getData()
    suspend fun saveData(userDataEntity: UserDataEntity) = repository.saveData(userDataEntity = userDataEntity)
    suspend fun deleteData() = repository.deleteData()
}