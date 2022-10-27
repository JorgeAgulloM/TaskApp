package com.softyorch.taskapp.ui.screens.login.model

import com.softyorch.taskapp.utils.emptyString

data class AccountModel(
    var userName: String,
    var userEmail: String,
    var userEmailRepeat: String,
    var userPass: String,
    var userPassRepeat: String,
) {
    companion object {
        val accountModel = AccountModel(
            userName = emptyString,
            userEmail = emptyString,
            userEmailRepeat = emptyString,
            userPass = emptyString,
            userPassRepeat = emptyString
        )
    }
}