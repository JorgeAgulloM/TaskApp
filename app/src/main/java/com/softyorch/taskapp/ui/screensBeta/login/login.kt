package com.softyorch.taskapp.ui.screensBeta.login

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
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
            delay(1000)
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

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Body(
    viewModel: LoginViewModelBeta,
    height: Dp,
    newAccount: Boolean,
    pexelsImage: MediaModel
) {
    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    val errorLoginModel by viewModel.errorsLogin.observeAsState(ErrorLoginModel.errorLoginModel)

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
                    LoginContent(
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
                    NewAccountContent(viewModel)
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

@Composable
private fun LoginContent(
    loginModel: LoginModel,
    errorLoginModel: ErrorLoginModel,
    onGo: (Boolean) -> Unit,
    onLoginInputChange: (LoginModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextFieldEmail(
            email = loginModel.userEmail,
            error = errorLoginModel.email,
            errorAccount = errorLoginModel.errorResultSignIn
        ) {
            onLoginInputChange(
                LoginModel(
                    userEmail = it.trim(),
                    userPass = loginModel.userPass,
                    rememberMe = loginModel.rememberMe
                )
            )
        }

        TextFieldPass(
            pass = loginModel.userPass,
            keyboardActions = KeyboardActions(onGo = { onGo(true) }),
            error = errorLoginModel.pass,
            errorAccount = errorLoginModel.errorResultSignIn
        ) {
            onLoginInputChange(
                LoginModel(
                    userEmail = loginModel.userEmail,
                    userPass = it.trim(),
                    rememberMe = loginModel.rememberMe
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            CheckerRememberMe(
                rememberMe = loginModel.rememberMe
            ) {
                onLoginInputChange(
                    LoginModel(
                        userEmail = loginModel.userEmail,
                        userPass = loginModel.userPass,
                        rememberMe = it
                    )
                )
            }
        }
    }
}

@Composable
fun NewAccountContent(viewModel: LoginViewModelBeta) {
    val newAccountModel by viewModel.newAccountModel.observeAsState(initial = NewAccountModel.newAccountModel)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldName(newAccountModel.userName, false, false, false) {}
        TextFieldEmail(newAccountModel.userEmail, false, false, false) {

        }
        TextFieldEmailRepeat(newAccountModel.userEmail, false, false, false) {

        }
        TextFieldPass(newAccountModel.userPass, true, keyboardActions = KeyboardActions(
            onGo = {
                //if (!newAccount) goOrErrorLogin = true
            }
        ), false, false) {}
        TextFieldPassRepeat(newAccountModel.userPass, false, keyboardActions = KeyboardActions(
            onGo = {
                //if (!newAccount) goOrErrorLogin = true
            }
        ), false, false) {}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldName(
    name: String,
    error: Boolean,
    errorAccount: Boolean,
    errorEmailExist: Boolean,
    onTextFieldChanged: (String) -> Unit
) {

    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = name,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.type_your_email),
                icon = Icons.Rounded.Email,
                contentDescription = stringResource(R.string.type_your_email),
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email
                ),
                isError = error || errorAccount || errorEmailExist,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error && !errorAccount) IconError(
            errorText = if (errorEmailExist) stringResource(R.string.error_email_exist)
            else stringResource(R.string.input_error_email)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldEmail(
    email: String,
    error: Boolean,
    errorAccount: Boolean = false,
    errorEmailExist: Boolean = false,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = email,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.type_your_email),
                icon = Icons.Rounded.Email,
                contentDescription = stringResource(R.string.type_your_email),
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email
                ),
                isError = error || errorAccount || errorEmailExist,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error && !errorAccount) IconError(
            errorText = if (errorEmailExist) stringResource(R.string.error_email_exist)
            else stringResource(R.string.input_error_email)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldEmailRepeat(
    email: String,
    error: Boolean,
    errorAccount: Boolean,
    errorEmailExist: Boolean,
    onTextFieldChanged: (String) -> Unit
) {

    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = email,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.type_your_email),
                icon = Icons.Rounded.Email,
                contentDescription = stringResource(R.string.type_your_email),
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email
                ),
                isError = error || errorAccount || errorEmailExist,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error && !errorAccount) IconError(
            errorText = if (errorEmailExist) stringResource(R.string.error_email_exist)
            else stringResource(R.string.input_error_email)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldPass(
    pass: String,
    newAccount: Boolean = false,
    keyboardActions: KeyboardActions,
    error: Boolean,
    errorAccount: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = pass,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.type_your_password),
                icon = Icons.Rounded.Key,
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (newAccount) ImeAction.Next else ImeAction.Go
                ),
                keyboardActions = keyboardActions,
                contentDescription = stringResource(R.string.type_your_password),
                isError = error || errorAccount,
                password = true,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error) IconError(
            errorText = if (errorAccount) stringResource(R.string.error_email_or_pass)
            else stringResource(R.string.input_error_pass)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldPassRepeat(
    pass: String,
    newAccount: Boolean,
    keyboardActions: KeyboardActions,
    error: Boolean,
    errorAccount: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = pass,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.type_your_password),
                icon = Icons.Rounded.Key,
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (newAccount) ImeAction.Next else ImeAction.Go
                ),
                keyboardActions = keyboardActions,
                contentDescription = stringResource(R.string.type_your_password),
                isError = error || errorAccount,
                password = true,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error) IconError(
            errorText = if (errorAccount) stringResource(R.string.error_email_or_pass)
            else stringResource(R.string.input_error_pass)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckerRememberMe(
    rememberMe: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    CheckCustom(
        checked = rememberMe,
        onCheckedChange = onCheckedChange,
        text = stringResource(R.string.remember_me),
        horizontalArrangement = Arrangement.End,
        onClick = {}
    )
}

@Composable
private fun BodyScreen(pexelsImage: MediaModel) {
    val pexelsUrl = stringResource(R.string.pexels_web)
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uriHandler = LocalUriHandler.current

        AppTitle()
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailsImageFromPexels(text = stringResource(R.string.pexels_courtesy)) {
                uriHandler.openUri(pexelsUrl)
            }
            DetailsImageFromPexels(text = stringResource(R.string.author) + pexelsImage.author) {
                uriHandler.openUri(pexelsImage.authorUrl)
            }
            DetailsImageFromPexels(text = stringResource(R.string.click_to_view)) {
                uriHandler.openUri(pexelsImage.imageUrl)
            }
        }
    }
}

@Composable
private fun AppTitle() {
    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    )
    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                brush = colorGradient,
                shape = MaterialTheme.shapes.large
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge.copy(
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DetailsImageFromPexels(
    text: String,
    onClick: () -> Unit
) {
    var click by remember { mutableStateOf(value = false) }
    val clickColor by click.contentColorLabelAsStateAnimation {
        if (click) {
            onClick()
            click = false
        }
    }
    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            clickColor.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    )

    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                brush = colorGradient,
                shape = MaterialTheme.shapes.large
            ).clickable {
                click = true
            },
    ) {
        androidx.compose.material3.Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}