/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

@Composable
fun NewAccountBody(
    isBlocked: Boolean,
    autoLogin: Boolean,
    newAccountModel: NewAccountModel,
    errorsNewAccount: ErrorNewAccountModel,
    hideNewAccount: () -> Unit,
    onNewAccountDataChange: (NewAccountModel) -> Unit,
    onClick: () -> Unit
) {
    Head("¿Ya tienes cuenta? ", "Inicia sesión", (isBlocked || autoLogin)) {
        hideNewAccount()
    }
    ContentNewAccount(
        newAccountModel,
        errorsNewAccount,
        onGo = { onClick() },
        onNewAccountDataChange
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