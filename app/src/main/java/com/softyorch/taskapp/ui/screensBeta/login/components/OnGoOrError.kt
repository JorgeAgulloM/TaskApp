/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import android.annotation.SuppressLint
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.screensBeta.login.LoginViewModelBeta
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.navigateTo
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnGoOrError(
    onGo: Boolean,
    autoLogin: Boolean,
    focusManager: FocusManager,
    scope: CoroutineScope,
    newAccount: Boolean,
    sheetState: BottomSheetState,
    navController: NavController,
    viewModel: LoginViewModelBeta,
    loginModel: LoginModel,
    newAccountModel: NewAccountModel,
    errorLoginModel: ErrorLoginModel
) {
    var onGo1 = onGo
    var showSnackBarErrors by rememberSaveable { mutableStateOf(value = false) }
    if (onGo1 || autoLogin) {
        focusManager.clearFocus()
        scope.launch {
            if (!newAccount) {
                if (autoLogin) {
                    sheetState.collapse()
                    withContext(Dispatchers.Main) {
                        delay(2000)
                        navigateTo(navController)
                    }
                } else {
                    viewModel.onLoginDataSend(loginModel).also {
                        if (it) showSnackBarErrors = true
                        else {
                            sheetState.collapse()
                            withContext(Dispatchers.Main) {
                                delay(500)
                                navigateTo(navController)
                            }
                        }
                    }
                }
            } else {
                viewModel.onNewAccountDataSend(newAccountModel).also {
                    if (it) showSnackBarErrors = true
                    else {
                        sheetState.collapse()
                        withContext(Dispatchers.Default) {
                            delay(500)
                            viewModel.showNewAccount()
                        }
                    }
                }
            }
            delay(500)
            onGo1 = false
        }
    }

    if (!errorLoginModel.error) showSnackBarErrors = false

    if (showSnackBarErrors) SnackBarErrorLoginNewAccount(newAccount) {
        showSnackBarErrors = false
    }
}