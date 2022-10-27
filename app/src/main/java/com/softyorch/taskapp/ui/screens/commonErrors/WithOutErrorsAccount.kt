/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import com.softyorch.taskapp.ui.screens.login.model.AccountModel

interface WithOutErrorsAccount :
    IsValidName, IsValidEmail, IsValidEmailRepeat, IsValidPass, IsValidPassRepeat,
    IsActivatedButton {

    fun withOutErrorsAccount(
        accountModel: AccountModel
    ): ErrorAccountModel {
        val errors = ErrorAccountModel()
        accountModel.apply {
            !isValidName(userName).also { errors.name = !it }
            !isValidEmail(userEmail).also { errors.email = !it }
            !isValidEmail(userEmail, userEmailRepeat).also { errors.emailRepeat = !it }
            !isValidPass(userPass).also { errors.pass = !it }
            !isValidPass(userPass, userPassRepeat).also { errors.passRepeat = !it }
            isActivatedButton(accountModel).also { errors.isActivatedButton = it }
        }

        errors.let {
            it.error = (it.name || it.email || it.emailRepeat || it.pass || it.passRepeat)
        }

        return errors
    }
}