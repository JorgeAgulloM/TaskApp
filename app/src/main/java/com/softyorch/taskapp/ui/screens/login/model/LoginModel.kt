package com.softyorch.taskapp.ui.screens.login.model

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.utils.emptyString

data class LoginModel(
    var userEmail: String,
    var userPass: String,
    var rememberMe: Boolean
) {
    companion object {
        val loginModelEmpty = LoginModel(
            userEmail = emptyString,
            userPass = emptyString,
            rememberMe = false
        )
    }
}

fun UserDataEntity.mapToLoginModel() = LoginModel(
    userEmail = this.userEmail,
    userPass = this.userPass,
    rememberMe = this.rememberMe
)
