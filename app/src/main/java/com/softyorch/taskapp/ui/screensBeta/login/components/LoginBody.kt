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
fun loginBody(
    isBlocked: Boolean,
    autoLogin: Boolean,
    viewModel: LoginViewModelBeta,
    loginModel: LoginModel,
    errorLoginModel: ErrorLoginModel,
    onGo: Boolean
): Boolean {
    var onGo1 = onGo
    Head("¿No tienes cuenta? ", "Crea una nueva", (isBlocked || autoLogin)) {
        viewModel.showNewAccount()
    }
    ContentLogin(
        loginModel,
        errorLoginModel,
        onGo = { onGo1 = it },
        viewModel::onLoginInputChange
    )
    Footer(
        text = stringResource(R.string.login),
        enable = errorLoginModel.isActivatedButton,
        error = errorLoginModel.error
    ) {
        onGo1 = true
    }
    return onGo1
}