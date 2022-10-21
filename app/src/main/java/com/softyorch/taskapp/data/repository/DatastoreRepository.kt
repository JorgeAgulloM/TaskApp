package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.database.datastore.DatastoreDataBase
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepository @Inject constructor(
    private val datastoreDataBase: DatastoreDataBase
) {
    fun getData() = datastoreDataBase.getData()
    suspend fun saveData(userDataEntity: UserDataEntity) = datastoreDataBase.saveData(userDataEntity = userDataEntity)
    suspend fun deleteData() = datastoreDataBase.deleteData()
}