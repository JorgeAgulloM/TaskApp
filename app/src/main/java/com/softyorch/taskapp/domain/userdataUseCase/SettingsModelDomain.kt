/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.SettingsModel
import com.softyorch.taskapp.ui.models.SettingsModelUi
import java.util.*

data class SettingsModelDomain(
    val id: UUID = UUID.randomUUID(),
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = true,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun SettingsModel.mapToSettingsModelDomain() = SettingsModelDomain(
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

fun SettingsModelUi.mapToSettingsModelDomain() = SettingsModelDomain(
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
