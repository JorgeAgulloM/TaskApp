/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.data.repository.user

import com.softyorch.taskapp.data.database.userdata.UserDataBaseDao
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.database.userdata.mapToUserDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(private val userDataBaseDao: UserDataBaseDao) {

    fun getUser(): Flow<UserModel> =
        userDataBaseDao.getData().map {
            it.mapToUserModel()
        }

    suspend fun saveUser(userModel: UserModel) {
        userDataBaseDao.apply {
            val user = getUserId(userModel.id.toString())?.copy(
                username = userModel.username,
                userEmail = userModel.userEmail,
                userPass = userModel.userPass,
                userPicture = userModel.userPicture
            )

            if (user != null) update(user)
        }
    }

    fun getSettings(): Flow<SettingsModel> =
        userDataBaseDao.getData().map {
            it.mapToSettingsModel()
        }

    suspend fun saveSettings(settingsModel: SettingsModel) {
        userDataBaseDao.apply {
            val settings = getUserId(settingsModel.id.toString())?.copy(
                lastLoginDate = settingsModel.lastLoginDate,
                rememberMe = settingsModel.rememberMe,
                lightDarkAutomaticTheme = settingsModel.lightDarkAutomaticTheme,
                lightOrDarkTheme = settingsModel.lightOrDarkTheme,
                automaticLanguage = settingsModel.automaticLanguage,
                automaticColors = settingsModel.automaticColors,
                timeLimitAutoLoading = settingsModel.timeLimitAutoLoading,
                textSize = settingsModel.textSize
            )

            if (settings != null) update(settings)
        }
    }

    suspend fun getUserDataEmail(email: String): UserDataEntity? =
        userDataBaseDao.getUserEmail(email = email)

    suspend fun singInUser(email: String, password: String): UserDataEntity? =
        userDataBaseDao.signIn(email = email, password = password)

    suspend fun addUserData(userModel: UserModel) =
        userDataBaseDao.insert(userModel.mapToUserDataEntity())

    suspend fun updateUserData(userDataEntity: UserDataEntity) =
        userDataBaseDao.update(userDataEntity = userDataEntity)
/*
    suspend fun deleteUserData(userDataEntity: UserDataEntity) =
        userDataBaseDao.delete(userDataEntity = userDataEntity)

    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()*/
}

