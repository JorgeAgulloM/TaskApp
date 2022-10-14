package com.softyorch.taskapp.ui.screensBeta.login.errors

import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

interface WithOutErrorsNewAccount :
    IsValidName, IsValidEmail, IsValidEmailRepeat, IsValidPass, IsValidPassRepeat {

    fun withOutErrorsNewAccount(
        newAccountModel: NewAccountModel
    ): ErrorNewAccountModel {
        val errors = ErrorNewAccountModel()
        newAccountModel.apply {
            !isValidName(name = userName).also { errors.name = !it }
            !isValidEmail(email = userEmail).also { errors.email = !it }
            !isValidEmail(email = userEmail, emailRepeat = userEmailRepeat).also { errors.emailRepeat = !it }
            !isValidPass(pass = userPass).also { errors.pass = !it }
            !isValidPass(pass = userPass, passRepeat = userPassRepeat).also { errors.passRepeat = !it }
        }

        errors.let {
            it.error = (it.name || it.email || it.emailRepeat || it.pass || it.passRepeat)
        }

        return errors
    }
}