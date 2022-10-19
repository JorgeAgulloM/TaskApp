/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screensBeta.login.components.*
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.*
import kotlin.math.absoluteValue

@Composable
fun LoginScreenBeta(navController: NavController) {

    val viewModel = hiltViewModel<LoginViewModelBeta>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val showBody: Boolean by viewModel.showBody.observeAsState(initial = false)
    val newAccount: Boolean by viewModel.showNewAccount.observeAsState(initial = false)
    val pexelsImage by viewModel.pexelsImage.observeAsState(initial = MediaModel.MediaModelEmpty)

    val screenHeightMid = LocalConfiguration.current.screenHeightDp / 2
    val screenHeightTwoThird = (LocalConfiguration.current.screenHeightDp / 5) * 4
    val height by newAccount.upDownIntegerAnimated(screenHeightTwoThird, screenHeightMid)

    if (showBody)
        Body(viewModel, navController, height.absoluteValue.dp, newAccount, isLoading, pexelsImage)
    if (isLoading)
        CircularIndicatorCustom(stringResource(R.string.loading_loading))

}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Body(
    viewModel: LoginViewModelBeta,
    navController: NavController,
    height: Dp,
    newAccount: Boolean,
    isBlocked: Boolean,
    pexelsImage: MediaModel
) {
    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    val errorLoginModel by viewModel.errorsLogin.observeAsState(ErrorLoginModel.errorLoginModel)

    val newAccountModel by viewModel.newAccountModel.observeAsState(NewAccountModel.newAccountModel)
    val errorsNewAccount by viewModel.errorsNewAccount.observeAsState(ErrorNewAccountModel.errorNewAccountModel)

    val autoLogin: Boolean by viewModel.autologin.observeAsState(initial = false)

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
    if (!autoLogin) scope.launch {
        delay(1000)
        sheetState.expand()
    }

    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    )

    var onGo by remember { mutableStateOf(value = false) }
    val focusManager = LocalFocusManager.current

    BottomSheetScaffold(
        modifier = Modifier.background(Color.Transparent),
        scaffoldState = scaffoldState,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeContentPadding()
                    .height(height)
                    .background(colorGradient),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!newAccount) {
                    LoginBody(
                        isBlocked, autoLogin, viewModel, loginModel, errorLoginModel
                    ){
                        onGo = true
                    }
                } else {
                    NewAccountBody(
                        isBlocked, autoLogin, viewModel, newAccountModel, errorsNewAccount
                    ){
                        onGo = true
                    }
                }

                if (onGo || autoLogin) {
                    OnGoOrError(
                        autoLogin, focusManager, scope, newAccount, sheetState, navController,
                        viewModel, loginModel, newAccountModel, errorLoginModel
                    ){
                        onGo = false
                    }
                }
            }
        }
    ) {
        if (autoLogin) CircularIndicatorCustom("AutoLogin...")
        Background(pexelsImage)
    }

}