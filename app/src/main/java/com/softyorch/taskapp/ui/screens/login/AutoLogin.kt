/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.login

import com.softyorch.taskapp.domain.userdataUseCase.GetSettings
import com.softyorch.taskapp.domain.userdataUseCase.SaveSettings
import com.softyorch.taskapp.domain.userdataUseCase.mapToSettingsModelDomain
import com.softyorch.taskapp.ui.models.SettingsModelUi
import com.softyorch.taskapp.ui.models.mapToSettingsModelUi
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.*
import kotlin.reflect.KProperty0

interface AutoLogin {
    suspend fun autologin(
        getSettings: KProperty0<GetSettings>,
        saveSettings: KProperty0<SaveSettings>
    ): Boolean {
        var isAuto = false
        getSettings()().first().let {
            val settings = it.mapToSettingsModelUi()
            if (settings.rememberMe)
                isAuto = isTimeOk(saveSettings, settings)
        }

        return isAuto
    }

    private suspend fun isTimeOk(
        saveSettings: KProperty0<SaveSettings>,
        user: SettingsModelUi
    ) = user.lastLoginDate?.time?.let { autoLoginLimit ->
        timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
            .compareTo(
                Date.from(Instant.now())
                    .time.minus(autoLoginLimit)
            ).let {
                if (it == 1) {
                    saveSettings()(user.mapToSettingsModelDomain())
                    true
                } else false
            }
    } ?: false

}
