package com.softyorch.taskapp.ui.screensBeta.login

import com.softyorch.taskapp.utils.emptyString

data class NewAccountModel(
    var username: String,
    var userEmail: String,
    var userPass: String,
) {
    companion object {
        val newAccountModel = NewAccountModel(
            username = emptyString,
            userEmail = emptyString,
            userPass = emptyString
        )
    }
}