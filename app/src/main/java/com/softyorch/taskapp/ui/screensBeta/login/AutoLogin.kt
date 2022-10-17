/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login

import android.util.Log
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.datastoreUseCase.SaveData
import com.softyorch.taskapp.domain.userdataUseCase.LoginUser
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import java.time.Instant
import java.util.*
import kotlin.reflect.KProperty0

interface AutoLogin {
    suspend fun autologin(
        datastoreUseCases: DatastoreUseCases,
        logIn: KProperty0<LoginUser>
    ): Boolean {
        var isAuto = false
        datastoreUseCases.getData().let { data ->
            data!!.flowOn(Dispatchers.IO).collect { user ->
                if (user.rememberMe) {
                    logIn()(user.userEmail, user.userPass).let { userLogin ->
                        if (userLogin != null) {
                            isAuto = isTimeOk(datastoreUseCases::saveData, userLogin)
                            Log.d("LOGIN", "login? -> $isAuto")
                        }
                    }
                }
            }
        }
        Log.d("LOGIN", "return -> $isAuto")
        return isAuto
    }

    private suspend fun isTimeOk(
        saveData: KProperty0<SaveData>,
        user: UserDataEntity
    ): Boolean {
        val timeWeekInMillis =
            timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
        user.lastLoginDate?.time?.let { autoLoginLimit ->
            val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
            timeWeekInMillis.compareTo(dif).let {
                if (it == 1) {
                    saveData()(userDataEntity = user)

                    return true
                }
            }
        }
        return false
    }
}