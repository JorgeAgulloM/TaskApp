package com.softyorch.taskapp.domain.model

import com.softyorch.taskapp.utils.emptyString
import java.util.*

data class Settings(
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
