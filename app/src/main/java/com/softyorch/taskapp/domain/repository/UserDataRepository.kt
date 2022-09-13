package com.softyorch.taskapp.domain.repository

import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.data.data.userdata.UserDataBaseDao
import com.softyorch.taskapp.domain.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(private val userDataBaseDao: UserDataBaseDao) {
    fun getAllUser(): Flow<List<UserData>> =
        userDataBaseDao.getAllUser().flowOn(Dispatchers.IO).conflate()

    suspend fun getUserDataId(id: String): Resource<UserData> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.getUserId(id = id)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun getUserDataEmail(email: String): Resource<UserData> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.getUserEmail(email = email)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun signInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<UserData> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.signIn(email = email, password = password)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun signInSharePreferences(email: String, password: String): Resource<UserData> {
        val response = try {
            Resource.Loading(data = true)
            userDataBaseDao.signIn(email = email, password = password)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun addUserData(userData: UserData): Boolean =
        getUserDataEmail(email = userData.userEmail).let {
            if (it.data?.userEmail.isNullOrEmpty()) {
                userDataBaseDao.insert(userData = userData)
                true
            } else false
        }


    suspend fun updateUserData(userData: UserData): Boolean =
        getUserDataEmail(email = userData.userEmail).let {
            if (it.data?.userEmail.isNullOrEmpty()){
                userDataBaseDao.update(userData = userData)
                true
            } else false
        }



    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()
    suspend fun deleteUserData(userData: UserData) = userDataBaseDao.delete(userData = userData)
}