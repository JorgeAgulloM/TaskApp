/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.data.repository.user

import com.softyorch.taskapp.data.database.userdata.UserDataBaseDao
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(private val userDataBaseDao: UserDataBaseDao) {

    suspend fun getSettings(): SettingsModel? =
        userDataBaseDao.getData()?.mapToSettingsModel()

    suspend fun saveSettings(settingsModel: SettingsModel) {
        userDataBaseDao.apply {
            val user = (getUserId(settingsModel.id.toString())?.copy(
                            lastLoginDate = settingsModel.lastLoginDate,
                            rememberMe = settingsModel.rememberMe,
                            lightDarkAutomaticTheme = settingsModel.lightDarkAutomaticTheme,
                            lightOrDarkTheme = settingsModel.lightOrDarkTheme,
                            automaticLanguage = settingsModel.automaticLanguage,
                            automaticColors = settingsModel.automaticColors,
                            timeLimitAutoLoading = settingsModel.timeLimitAutoLoading,
                            textSize = settingsModel.textSize
                        )
                    )
            if (user != null) update(user)
        }
    }

    suspend fun getUserDataEmail(email: String): UserDataEntity? =
        userDataBaseDao.getUserEmail(email = email)

    suspend fun singInUser(email: String, password: String): UserDataEntity? =
        userDataBaseDao.signIn(email = email, password = password)

    suspend fun addUserData(userDataEntity: UserDataEntity) =
        userDataBaseDao.insert(userDataEntity = userDataEntity)

    suspend fun updateUserData(userDataEntity: UserDataEntity) =
        userDataBaseDao.update(userDataEntity = userDataEntity)
/*
    suspend fun deleteUserData(userDataEntity: UserDataEntity) =
        userDataBaseDao.delete(userDataEntity = userDataEntity)

    suspend fun deleteAllUsers() = userDataBaseDao.deleteAll()*/
}

