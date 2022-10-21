/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.datastoreUseCase.SaveData
import com.softyorch.taskapp.domain.userdataUseCase.LoginUser
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import okhttp3.internal.notify
import java.time.Instant
import java.util.*
import kotlin.reflect.KProperty0

interface AutoLogin {
    @OptIn(FlowPreview::class)
    suspend fun autologin(
        datastoreUseCases: DatastoreUseCases,
        logIn: KProperty0<LoginUser>
    ): Boolean {
        var isAuto = false
        datastoreUseCases.getData().debounce(2000)
            .catch { ex ->
                ex.notify()
                /**No se para que sirve notify()*/
            }.first().let {
                if (it.rememberMe) logIn()(it.userEmail, it.userPass)?.let { user ->
                    isAuto = isTimeOk(datastoreUseCases::saveData, user)
                }
            }

        return isAuto
    }

    private suspend fun isTimeOk(
        saveData: KProperty0<SaveData>,
        user: UserDataEntity
    ) = user.lastLoginDate?.time?.let { autoLoginLimit ->
        timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
            .compareTo(
                Date.from(Instant.now())
                    .time.minus(autoLoginLimit)
            ).let {
                if (it == 1) {
                    saveData()(userDataEntity = user)
                    true
                } else false
            }
    } ?: false

}
