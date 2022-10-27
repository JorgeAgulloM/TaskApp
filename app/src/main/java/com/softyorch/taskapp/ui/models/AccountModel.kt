/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
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

fun UserDataEntity.mapToAccountModel(): AccountModel = AccountModel(
    userName = this.username,
    userEmail = this.userEmail,
    userEmailRepeat = this.userEmail,
    userPass = this.userPass,
    userPassRepeat = this.userPass
)