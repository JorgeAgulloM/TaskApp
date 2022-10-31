/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screens.login.model.LoginModel

interface WithOutErrorsLogin : IsValidEmail, IsValidPass, IsActivatedButton {

    fun withOutErrorsLogin(loginModel: LoginModel): ErrorLoginModel {
        val errors = ErrorLoginModel()
        loginModel.apply {
            !isValidEmail(userEmail).also { errors.email = !it }
            !isValidPass(userPass).also { errors.pass = !it }
            isActivatedButton(loginModel).also { errors.isActivatedButton = it }
        }
        errors.let { it.error = (it.email || it.pass) }

        return errors
    }
}