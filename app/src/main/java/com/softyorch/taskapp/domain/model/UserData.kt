package com.softyorch.taskapp.domain.model

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.utils.emptyString
import java.util.*

data class UserData(
    var username: String,
    var userEmail: String,
    var userPass: String,
    var userPicture: String = emptyString,
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean = false,
    var lightDarkAutomaticTheme: Boolean = true,
    var lightOrDarkTheme: Boolean = false,
    var automaticLanguage: Boolean = true,
    var automaticColors: Boolean = false,
    var timeLimitAutoLoading: Int = 1, //One week
    var textSize: Int = 0
)

fun UserDataEntity.toDomain() = UserData(
    username,
    userEmail,
    userPass,
    userPicture,
    lastLoginDate,
    rememberMe,
    lightDarkAutomaticTheme,
    lightOrDarkTheme
)
