/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screensBeta.login.LoginViewModelBeta
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

@Composable
fun NewAccountBody(
    isBlocked: Boolean,
    autoLogin: Boolean,
    viewModel: LoginViewModelBeta,
    newAccountModel: NewAccountModel,
    errorsNewAccount: ErrorNewAccountModel,
    onClick: () -> Unit
) {
    Head("¿Ya tienes cuenta? ", "Inicia sesión", (isBlocked || autoLogin)) {
        viewModel.showNewAccount()
    }
    ContentNewAccount(
        newAccountModel,
        errorsNewAccount,
        onGo = { onClick() },
        viewModel::onNewAccountInputChange
    )
    Footer(
        text = stringResource(R.string.new_account),
        enable = errorsNewAccount.isActivatedButton,
        error = errorsNewAccount.error
    ) {
        Log.d("LOGIN", "newAccountBody.()Footer")
        onClick()
    }
}