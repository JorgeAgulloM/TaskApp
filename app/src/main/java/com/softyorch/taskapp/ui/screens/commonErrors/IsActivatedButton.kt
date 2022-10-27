/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.ui.screens.login.model.LoginModel
import com.softyorch.taskapp.ui.screens.login.model.AccountModel

interface IsActivatedButton {
    fun isActivatedButton(loginModel: LoginModel): Boolean =
        loginModel.userEmail.isNotEmpty() && loginModel.userPass.isNotEmpty()

    fun isActivatedButton(
        accountModel: AccountModel
    ): Boolean =
        accountModel.userName.isNotEmpty() &&
        accountModel.userEmail.isNotEmpty() &&
        accountModel.userEmailRepeat.isNotEmpty() &&
        accountModel.userPass.isNotEmpty() &&
        accountModel.userPassRepeat.isNotEmpty()
}