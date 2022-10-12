package com.softyorch.taskapp.ui.screensBeta.login.model

import com.softyorch.taskapp.utils.emptyString

data class LoginModel(
    var userEmail: String,
    var userPass: String,
) {
    companion object {
        val loginModelEmpty = LoginModel(
            userEmail = emptyString,
            userPass = emptyString
        )
    }
}
