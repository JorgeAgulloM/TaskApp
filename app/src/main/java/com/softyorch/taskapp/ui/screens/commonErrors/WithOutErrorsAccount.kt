/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorNewModel
import com.softyorch.taskapp.ui.screens.login.model.NewAccountModel

interface WithOutErrorsAccount :
    IsValidName, IsValidEmail, IsValidEmailRepeat, IsValidPass, IsValidPassRepeat,
    IsActivatedButton {

    fun withOutErrorsNewAccount(
        newAccountModel: NewAccountModel
    ): ErrorNewModel {
        val errors = ErrorNewModel()
        newAccountModel.apply {
            !isValidName(userName).also { errors.name = !it }
            !isValidEmail(userEmail).also { errors.email = !it }
            !isValidEmail(userEmail, userEmailRepeat).also { errors.emailRepeat = !it }
            !isValidPass(userPass).also { errors.pass = !it }
            !isValidPass(userPass, userPassRepeat).also { errors.passRepeat = !it }
            isActivatedButton(newAccountModel).also { errors.isActivatedButton = it }
        }

        errors.let {
            it.error = (it.name || it.email || it.emailRepeat || it.pass || it.passRepeat)
        }

        return errors
    }
}