package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserDataBaseDao
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
/*import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn*/
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(private val userDataBaseDao: UserDataBaseDao) {
/*    fun getAllUser(): Flow<List<UserDataEntity>> =
        userDataBaseDao.getAllUser().flowOn(Dispatchers.IO).conflate()

    suspend fun getUserDataId(id: String): Resource<UserDataEntity> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.getUserId(id = id)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }*/

    suspend fun getUserDataEmail(email: String): Resource<UserDataEntity> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.getUserEmail(email = email)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun singInUser(
        email: String,
        password: String
    ): Resource<UserDataEntity> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.signIn(email = email, password = password)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun addUserData(userDataEntity: UserDataEntity): Boolean =
        getUserDataEmail(email = userDataEntity.userEmail).let {
            if (it.data?.userEmail.isNullOrEmpty()) {
                userDataBaseDao.insert(userDataEntity = userDataEntity)
                true
            } else false
        }


    suspend fun updateUserData(userDataEntity: UserDataEntity): Boolean =
        getUserDataEmail(email = userDataEntity.userEmail).let {
            if (!it.data?.userEmail.isNullOrEmpty()){
                userDataBaseDao.update(userDataEntity = userDataEntity)
                true
            } else false
        }



/*
    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()
    suspend fun deleteUserData(userDataEntity: UserDataEntity) = userDataBaseDao.delete(userDataEntity = userDataEntity)
*/

}