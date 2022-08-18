package com.softyorch.taskapp.utils.login

import java.util.*

data class LoginState(
    //val id: UUID = UUID.randomUUID(),
    var username: String = "",
    var userEmail: String = "",
    var userPass: String = "",
    var userPicture: String? = null,
    var lastLoginDate: Date? = null,
    var rememberMe: Boolean? = false
)
