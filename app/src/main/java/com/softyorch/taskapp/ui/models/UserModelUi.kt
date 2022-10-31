/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.userdataUseCase.UserModelDomain
import com.softyorch.taskapp.utils.emptyString
import java.util.*

data class UserModelUi(
    var id: UUID = UUID.randomUUID(),
    var userName: String,
    var userEmail: String,
    var userEmailRepeat: String,
    var userPass: String,
    var userPassRepeat: String,
) {
    companion object {
        val userModelUi = UserModelUi(
            id = UUID.randomUUID(),
            userName = emptyString,
            userEmail = emptyString,
            userEmailRepeat = emptyString,
            userPass = emptyString,
            userPassRepeat = emptyString
        )
    }
}

fun UserModelDomain.mapToUserModelUI() = UserModelUi(
    id = this.id,
    userName = this.username,
    userEmail = this.userEmail,
    userEmailRepeat = this.userEmail,
    userPass = this.userPass,
    userPassRepeat = this.userPass
)