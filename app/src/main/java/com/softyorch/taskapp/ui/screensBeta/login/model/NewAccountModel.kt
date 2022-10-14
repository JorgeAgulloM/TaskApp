package com.softyorch.taskapp.ui.screensBeta.login.model

import com.softyorch.taskapp.utils.emptyString

data class NewAccountModel(
    var userName: String,
    var userEmail: String,
    var userEmailRepeat: String,
    var userPass: String,
    var userPassRepeat: String,
) {
    companion object {
        val newAccountModel = NewAccountModel(
            userName = emptyString,
            userEmail = emptyString,
            userEmailRepeat = emptyString,
            userPass = emptyString,
            userPassRepeat = emptyString
        )
    }
}