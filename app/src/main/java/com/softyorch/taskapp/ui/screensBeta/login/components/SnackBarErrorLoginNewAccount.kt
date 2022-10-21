/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.SnackBarError

@Composable
fun SnackBarErrorLoginNewAccount(
    newAccount: Boolean,
    onClick: () -> Unit
) {
    SnackBarError(
        errorText = if (newAccount) stringResource(R.string.error_email_exist)
        else stringResource(R.string.error_email_or_pass)
    ) {
        onClick()
    }
}