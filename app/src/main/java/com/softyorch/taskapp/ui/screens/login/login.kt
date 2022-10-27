/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.login

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.screens.login.components.*
import com.softyorch.taskapp.ui.screens.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screens.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screens.login.model.LoginModel
import com.softyorch.taskapp.ui.screens.login.model.MediaModel
import com.softyorch.taskapp.ui.screens.login.model.NewAccountModel
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreenBeta(navController: NavController) {

    val viewModel = hiltViewModel<LoginViewModelBeta>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val showBody: Boolean by viewModel.showBody.observeAsState(initial = false)
    val newAccount: Boolean by viewModel.showNewAccount.observeAsState(initial = false)
    val autoLogin: Boolean by viewModel.autologin.observeAsState(initial = false)
    val loginSuccess: Boolean by viewModel.loginSuccess.observeAsState(initial = false)

    val screenHeightMid = LocalConfiguration.current.screenHeightDp / 2
    val screenHeightTwoThird = (LocalConfiguration.current.screenHeightDp / 5) * 4
    val height by newAccount.upDownIntegerAnimated(screenHeightTwoThird, screenHeightMid)

    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    val errorsLoginModel by viewModel.errorsLogin.observeAsState(ErrorLoginModel.errorLoginModel)

    val newAccountModel by viewModel.newAccountModel.observeAsState(NewAccountModel.newAccountModel)
    val errorsNewAccount by viewModel.errorsNewAccount.observeAsState(ErrorNewAccountModel.errorNewAccountModel)

    val pexelsImage by viewModel.pexelsImage.observeAsState(initial = MediaModel.MediaModelEmpty)

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    if (showBody && (!autoLogin || !loginSuccess)) scope.launch {
        delay(3000)
        if (!autoLogin) if (!loginSuccess) sheetState.expand()
    }

    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    )

    var showSnackBarErrors by rememberSaveable { mutableStateOf(value = false) }
    var onGo by remember { mutableStateOf(value = false) }
    val focusManager = LocalFocusManager.current

    if (autoLogin && !onGo) scope.launch {
        onGo = true
        sheetState.collapse()
        withContext(Dispatchers.Main) {
            focusManager.clearFocus()
            delay(3000)
            navigateTo(navController)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.background(Color.Transparent),
        scaffoldState = scaffoldState,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetGesturesEnabled = false,
        sheetElevation = ELEVATION_DP,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp)
                    .background(colorGradient),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (showBody) {
                    if (newAccount) {
                        NewAccountBody(
                            isLoading, autoLogin, newAccountModel, errorsNewAccount,
                            viewModel::hideNewAccount, viewModel::onNewAccountInputChange
                        ) {
                            focusManager.clearFocus()
                            scope.launch {
                                viewModel.onNewAccountDataSend(newAccountModel).also {
                                    if (it) {
                                        if (errorsNewAccount.emailExists) showSnackBarErrors = true
                                    } else {
                                        sheetState.collapse()
                                        withContext(Dispatchers.Default) {
                                            delay(500)
                                            viewModel.hideNewAccount()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LoginBody(
                            isLoading, autoLogin, loginModel, errorsLoginModel,
                            viewModel::showNewAccount, viewModel::onLoginInputChange
                        ) {
                            focusManager.clearFocus()
                            scope.launch {
                                viewModel.onLoginDataSend(loginModel).also {
                                    if (it) {
                                        if (errorsLoginModel.errorResultSignIn) showSnackBarErrors = true
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            delay(2000)
                                            navigateTo(navController)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showSnackBarErrors) {
                    //if (!errorLoginModel.error && !errorsNewAccount.error) showSnackBarErrors = false
                    SnackBarErrorLoginNewAccount(newAccount) {
                        showSnackBarErrors = false
                    }
                }
            }
        }
    ) {
        Background(pexelsImage)
    }

    if (isLoading) CircularIndicatorCustom(stringResource(R.string.loading_loading))
    if (autoLogin) CircularIndicatorCustom(stringResource(R.string.auto_login))
    if (loginSuccess) CircularIndicatorCustom(stringResource(R.string.loading_login))

}

fun navigateTo(navController: NavController) {
    navController.navigate(AppScreensRoutes.MainScreenBeta.route) {
        navController.backQueue.clear()
    }
}