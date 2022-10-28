/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.login.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import com.softyorch.taskapp.ui.models.UserModelUi

@Composable
fun NewAccountBody(
    isLoading: Boolean,
    autoLogin: Boolean,
    userModelUi: UserModelUi,
    errorsNewAccount: ErrorAccountModel,
    hideNewAccount: () -> Unit,
    onNewAccountDataChange: (UserModelUi) -> Unit,
    onClick: () -> Unit
) {
    Head("¿Ya tienes cuenta? ", "Inicia sesión", (isLoading || autoLogin)) {
        hideNewAccount()
    }
    ContentNewAccount(
        userModelUi,
        errorsNewAccount,
        onGo = { onClick() },
        onNewAccountDataChange
    )
    Footer(
        text = stringResource(R.string.new_account),
        enable = errorsNewAccount.isActivatedButton || isLoading,
        error = errorsNewAccount.error
    ) {
        Log.d("LOGIN", "newAccountBody.()Footer")
        onClick()
    }
}