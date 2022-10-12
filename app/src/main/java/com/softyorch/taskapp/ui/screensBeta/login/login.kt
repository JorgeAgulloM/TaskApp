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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.extensions.contentColorLabelAsStateAnimation
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = pexelsImage.image,
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            onSuccess = { onLoadImage() }
        )
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
                    LoginContent(viewModel)
                    Footer(text = stringResource(R.string.login)) {}
                } else {
                    Head(text1 = "¿Ya tienes cuenta? ", text2 = "Inicia sesión") {
                        viewModel.showNewAccount()
                    }
                    NewAccountContent(viewModel)
                    Footer(text = stringResource(R.string.new_account)) {}
                }
            }
        }
    ) {
        Background(pexelsImage) {
/*            scope.launch {
                delay(2000)
                sheetState.expand()
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
            style = MaterialTheme.typography.labelSmall,
            color = colorText
        )
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
        TextFieldPass(newAccountModel.userPass, false, keyboardActions = KeyboardActions(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(viewModel: LoginViewModelBeta) {
    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    var checkState by remember { mutableStateOf(value = false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldEmail(loginModel.userEmail, false, false, false) {

        }
        TextFieldPass(loginModel.userPass, false, keyboardActions = KeyboardActions(
            onGo = {
                //if (!newAccount) goOrErrorLogin = true
            }
        ), false, false) {

        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            CheckCustom(
                checked = checkState,
                onCheckedChange = { checkState = it },
                text = stringResource(R.string.remember_me),
                onClick = {}
            )
        }
    }
}

@Composable
private fun Footer(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        ButtonCustom(
            text = text,
            primary = true,
            enable = true,
            error = false
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
