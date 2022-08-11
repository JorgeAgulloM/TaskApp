package com.softyorch.taskapp.repository

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.userdata.UserDataBaseDao
import com.softyorch.taskapp.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserDataRepository @Inject constructor(private val userDataBaseDao: UserDataBaseDao) {
    fun getAllUser(): Flow<List<UserData>> =
        userDataBaseDao.getAllUser().flowOn(Dispatchers.IO).conflate()

    suspend fun getUserDataId(id: String): Resource<UserData> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.getUserId(id = id)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
    suspend fun addUserData(userData: UserData) = userDataBaseDao.insert(userData = userData)
    suspend fun updateUserData(userData: UserData) = userDataBaseDao.update(userData = userData)
    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()
    suspend fun deleteUserData(userData: UserData) = userDataBaseDao.delete(userData = userData)
}