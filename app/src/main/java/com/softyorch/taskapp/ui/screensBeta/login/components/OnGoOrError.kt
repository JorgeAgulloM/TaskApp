/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.screensBeta.login.LoginViewModelBeta
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnGoOrError(
    autoLogin: Boolean,
    focusManager: FocusManager,
    scope: CoroutineScope,
    newAccount: Boolean,
    sheetState: BottomSheetState,
    navController: NavController,
    viewModel: LoginViewModelBeta,
    loginModel: LoginModel,
    newAccountModel: NewAccountModel,
    errorLoginModel: ErrorLoginModel,
    onClick: () -> Unit
) {
    var onGo by remember { mutableStateOf(value = false) }
    var showSnackBarErrors by rememberSaveable { mutableStateOf(value = false) }
    if (!showSnackBarErrors && !onGo) {
        Log.d("LOGIN", "OnGoOrError.!showSnack")
        focusManager.clearFocus()
        scope.launch {
            if (!newAccount) {
                if (autoLogin) {
                    onGo = true
                    sheetState.collapse()
                    withContext(Dispatchers.Main) {
                        delay(2000)
                        navigateTo(navController)
                    }
                } else {
                    onGo = true
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
                onGo = true
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
        }
    } else if (showSnackBarErrors) {
        if (!errorLoginModel.error) showSnackBarErrors = false
        SnackBarErrorLoginNewAccount(newAccount) {
            onClick()
            showSnackBarErrors = false
        }
    }
}

fun navigateTo(navController: NavController) {
    navController.navigate(AppScreensRoutes.MainScreenBeta.route) {
        navController.backQueue.clear()
    }
}