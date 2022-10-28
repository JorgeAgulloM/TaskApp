/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.ui.screens.login.model.LoginModel
import com.softyorch.taskapp.ui.models.UserModelUi

interface IsActivatedButton {
    fun isActivatedButton(loginModel: LoginModel): Boolean =
        loginModel.userEmail.isNotEmpty() && loginModel.userPass.isNotEmpty()

    fun isActivatedButton(
        userModelUi: UserModelUi
    ): Boolean =
        userModelUi.userName.isNotEmpty() &&
        userModelUi.userEmail.isNotEmpty() &&
        userModelUi.userEmailRepeat.isNotEmpty() &&
        userModelUi.userPass.isNotEmpty() &&
        userModelUi.userPassRepeat.isNotEmpty()
}