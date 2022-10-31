/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.userdataUseCase.SettingsModelDomain
import java.util.*

data class SettingsModelUi(
    var id: UUID = UUID.randomUUID(),
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = false,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun SettingsModelDomain.mapToSettingsModelUi() = SettingsModelUi(
    id,
    lastLoginDate,
    rememberMe,
    lightDarkAutomaticTheme,
    lightOrDarkTheme,
    automaticLanguage,
    automaticColors,
    timeLimitAutoLoading,
    textSize
)
