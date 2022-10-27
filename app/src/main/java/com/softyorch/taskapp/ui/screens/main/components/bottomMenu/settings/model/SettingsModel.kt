/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.settings.model

import com.softyorch.taskapp.domain.userdataUseCase.UserDataModelDomain
import java.util.*

data class SettingsModel(
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = true,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun UserDataModelDomain.mapToSettingsModel(): SettingsModel = SettingsModel(
    lastLoginDate = this.lastLoginDate,
    rememberMe = this.rememberMe,
    lightDarkAutomaticTheme = this.lightDarkAutomaticTheme,
    lightOrDarkTheme = this.lightOrDarkTheme,
    automaticLanguage = this.automaticLanguage,
    automaticColors = this.automaticColors,
    timeLimitAutoLoading = this.timeLimitAutoLoading,
    textSize = this.textSize
)
