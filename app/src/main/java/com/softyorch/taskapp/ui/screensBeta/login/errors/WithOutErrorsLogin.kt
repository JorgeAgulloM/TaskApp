package com.softyorch.taskapp.ui.screensBeta.login.errors

import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel

interface WithOutErrorsLogin : IsValidEmail, IsValidPass {

    fun withOutErrorsLogin(loginModel: LoginModel): ErrorLoginModel {
        val errors = ErrorLoginModel()
        !isValidEmail(email = loginModel.userEmail).also { errors.email = !it }
        !isValidPass(pass = loginModel.userPass).also { errors.pass = !it }

        errors.let { it.error = (it.email || it.pass) }

        return errors
    }
}