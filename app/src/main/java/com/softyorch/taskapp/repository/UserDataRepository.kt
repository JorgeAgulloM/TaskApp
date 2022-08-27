package com.softyorch.taskapp.repository

import android.util.Log
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.userdata.UserDataBaseDao
import com.softyorch.taskapp.model.UserData
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

    suspend fun addUserData(userData: UserData): Boolean {
        Log.d("LOGIN", "UserDataRepository.UserData recibido -> $userData")
        return getUserDataEmail(email = userData.userEmail).let {
            if (it.data?.userEmail.isNullOrEmpty()) {
                Log.d("LOGIN", "UserDataRepository.Insert isNullOrEmpty -> true")
                val user = userDataBaseDao.insert(userData = userData)
                Log.d("LOGIN", "UserDataRepository.create user -> $user")
                true
            } else {
                Log.d("LOGIN", "UserDataRepository.Insert isNullOrEmpty -> false")
                false
            }
        }
    }

    suspend fun updateUserData(userData: UserData) = userDataBaseDao.update(userData = userData)
    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()
    suspend fun deleteUserData(userData: UserData) = userDataBaseDao.delete(userData = userData)
}