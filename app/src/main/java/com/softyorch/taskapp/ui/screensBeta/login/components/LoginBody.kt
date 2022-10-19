/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screensBeta.login.LoginViewModelBeta
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel

@Composable
fun LoginBody(
    isBlocked: Boolean,
    autoLogin: Boolean,
    viewModel: LoginViewModelBeta,
    loginModel: LoginModel,
    errorLoginModel: ErrorLoginModel,
    onClick: () -> Unit
) {
    Head(
        stringResource(R.string.not_have_account),
        stringResource(R.string.create_account),
        (isBlocked || autoLogin)
    ) {
        viewModel.showNewAccount()
    }
    ContentLogin(
        loginModel,
        errorLoginModel,
        onGo = { onClick() },
        viewModel::onLoginInputChange
    )
    Footer(
        text = stringResource(R.string.login),
        enable = errorLoginModel.isActivatedButton,
        error = errorLoginModel.error
    ) {
        onClick()
    }
}