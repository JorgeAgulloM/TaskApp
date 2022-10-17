/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.errors

import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

interface IsActivatedButton {
    fun isActivatedButton(loginModel: LoginModel): Boolean =
        loginModel.userEmail.isNotEmpty() && loginModel.userPass.isNotEmpty()

    fun isActivatedButton(
        newAccountModel: NewAccountModel
    ): Boolean =
        newAccountModel.userName.isNotEmpty() &&
        newAccountModel.userEmail.isNotEmpty() &&
        newAccountModel.userEmailRepeat.isNotEmpty() &&
        newAccountModel.userPass.isNotEmpty() &&
        newAccountModel.userPassRepeat.isNotEmpty()
}