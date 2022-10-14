package com.softyorch.taskapp.ui.screensBeta.login

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screensBeta.login.components.*
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import com.softyorch.taskapp.utils.extensions.contentColorLabelAsStateAnimation
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

/**
 * Task-App
 * File created by Jorge Agulló on 11/November/2022
 */

@Preview(showBackground = true)
@Composable
fun LoginScreenBeta() {

    val scope = rememberCoroutineScope()
    val viewModel = hiltViewModel<LoginViewModelBeta>()
    val showLogin by viewModel.showLogin.observeAsState(initial = false)
    val newAccount by viewModel.showNewAccount.observeAsState(initial = false)
    val pexelsImage by viewModel.pexelsImage.observeAsState(initial = MediaModel.MediaModelEmpty)
    val screenHeightMid = LocalConfiguration.current.screenHeightDp / 2
    val screenHeightTwoThird = (LocalConfiguration.current.screenHeightDp / 3) * 2
    val height by newAccount.upDownIntegerAnimated(screenHeightTwoThird, screenHeightMid)

    Background(pexelsImage) {
        scope.launch {
            delay(2000)
            viewModel.showLogin()
        }
    }
    if (!showLogin) {
        CircularIndicatorCustom(stringResource(R.string.loading_loading))
    } else {
        Body(viewModel, height.absoluteValue.dp, newAccount, pexelsImage)
    }
}

@Composable
fun Background(pexelsImage: MediaModel, onLoadImage: () -> Unit) {
    var showInfo by remember { mutableStateOf(value = false) }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        AsyncImage(
            model = pexelsImage.image,
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            onSuccess = {
                onLoadImage()
                showInfo = true
            }
        )
        if (showInfo) BodyScreen(pexelsImage)
    }
}

@Composable
private fun Head(text1: String, text2: String, onClick: () -> Unit) {
    var click by remember { mutableStateOf(value = false) }
    val colorText by click.contentColorLabelAsStateAnimation {
        onClick()
        click = false
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = text1,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(top = 4.dp).clickable { click = true },
            text = text2,
            style = MaterialTheme.typography.labelSmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = colorText
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Body(
    viewModel: LoginViewModelBeta,
    height: Dp,
    newAccount: Boolean,
    pexelsImage: MediaModel
) {
    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    val errorLoginModel by viewModel.errorsLogin.observeAsState(ErrorLoginModel.errorLoginModel)

    val errorsNewAccount by viewModel.errorsNewAccount.observeAsState(ErrorNewAccountModel.errorNewAccountModel)


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
    scope.launch {
        delay(500)
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
                    Head(text1 = "¿No tienes cuenta? ", text2 = "Crea una nueva") {
                        viewModel.showNewAccount()
                    }
                    ContentLogin(
                        loginModel,
                        errorLoginModel,
                        onGo = { onGo = it },
                        viewModel::onLoginInputChange
                    )
                    Footer(stringResource(R.string.login), error = errorLoginModel.error) {
                        onGo = true
                    }
                } else {
                    Head(text1 = "¿Ya tienes cuenta? ", text2 = "Inicia sesión") {
                        viewModel.showNewAccount()
                    }
                    ContentNewAccount(viewModel)
                    Footer(text = stringResource(R.string.new_account), errorLoginModel.error) {}
                }

                var showSnackBarErrors by rememberSaveable { mutableStateOf(value = false) }
                if (onGo) {
                    focusManager.clearFocus()
                    scope.launch {
                        viewModel.onLoginDataSend(loginModel).also {
                            if (it) showSnackBarErrors = true
                            else {
                                sheetState.collapse()
                                withContext(Dispatchers.Default){
                                    delay(500)
                                    /** TODO navigation **/
                                }
                            }
                        }
                        delay(500)
                        onGo = false
                    }
                }

                if (!errorLoginModel.error) showSnackBarErrors = false

                if (showSnackBarErrors) SnackBarError(
                    errorText = if (errorLoginModel.errorResultSignIn) stringResource(R.string.error_email_exist)
                    else if (errorLoginModel.email || errorLoginModel.pass) stringResource(R.string.error_email_or_pass)
                    else stringResource(R.string.snack_input_error)
                ) {
                    showSnackBarErrors = false
                }
            }
        }
    ) {
        Background(pexelsImage) {
            /*scope.launch {
                delay(1000)
            }*/
        }
    }

}

@Composable
private fun Footer(text: String, error: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        ButtonCustom(
            text = text,
            primary = true,
            enable = true,
            error = error
        ) {
            onClick()
        }
    }
}