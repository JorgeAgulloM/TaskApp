/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.data.repository.user

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.userdataUseCase.SettingsModelDomain
import java.util.*

data class SettingsModel(
    val id: UUID = UUID.randomUUID(),
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = false,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun UserDataEntity.mapToSettingsModel() = SettingsModel(
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

fun SettingsModelDomain.mapToSettingsModel() = SettingsModel(
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
