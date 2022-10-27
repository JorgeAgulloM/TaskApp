/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase


import com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.settings.model.SettingsModel
import com.softyorch.taskapp.utils.*
import java.util.*

data class UserDataModelDomain(
    val id: UUID = UUID.randomUUID(),
    var username: String = emptyString,
    var userEmail: String = emptyString,
    var userPass: String = emptyString,
    var userPicture: String = emptyString,
    /**
     * User Settings
     */
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = true,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun SettingsModel.mapToUserDataModelDomain(): UserDataModelDomain = UserDataModelDomain(
    lastLoginDate = this.lastLoginDate,
    rememberMe = this.rememberMe,
    lightDarkAutomaticTheme = this.lightDarkAutomaticTheme,
    lightOrDarkTheme = this.lightOrDarkTheme,
    automaticLanguage = this.automaticLanguage,
    automaticColors = this.automaticColors,
    timeLimitAutoLoading = this.timeLimitAutoLoading,
    textSize = this.textSize
)

