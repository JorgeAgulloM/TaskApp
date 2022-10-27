/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screens.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screens.login.model.LoginModel

@Composable
fun LoginBody(
    isLoading: Boolean,
    autoLogin: Boolean,
    loginModel: LoginModel,
    errorLoginModel: ErrorLoginModel,
    showNewAccount: () -> Unit,
    onLoginInputChange: (LoginModel) -> Unit,
    onClick: () -> Unit
) {
    Head(
        stringResource(R.string.not_have_account),
        stringResource(R.string.create_account),
        (isLoading || autoLogin)
    ) {
        showNewAccount()
    }
    ContentLogin(
        loginModel,
        errorLoginModel,
        onGo = { onClick() },
        onLoginInputChange
    )
    Footer(
        text = stringResource(R.string.login),
        enable = errorLoginModel.isActivatedButton || isLoading,
        error = errorLoginModel.error
    ) {
        onClick()
    }
}