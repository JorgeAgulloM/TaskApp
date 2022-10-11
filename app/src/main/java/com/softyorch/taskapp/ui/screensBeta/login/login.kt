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
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Task-App
 * File created by Jorge Agulló on 11/November/2022
 */

@Preview(showBackground = true)
@Composable
fun LoginScreenBeta(navController: NavController = NavController(context = LocalContext.current)) {

    val viewModel = hiltViewModel<LoginViewModelBeta>()
    val showLogin by viewModel.showLogin.observeAsState(initial = false)

    TaskAppTheme {
        Surface(Modifier.fillMaxSize()) {
            Background {
                viewModel.showLogin()
            }
            if (!showLogin) {
                CircularIndicatorCustom(stringResource(R.string.loading_loading))
            } else {
                Body(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Background(onLoadImage: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = R.drawable.pexels_polina_kovaleva_5717421,
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            onSuccess = { onLoadImage() }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Body(viewModel: LoginViewModelBeta) {
    val newAccount by viewModel.showNewAccount.observeAsState(initial = false)
    val loginModel by viewModel.loginModel.observeAsState(initial = LoginModel.loginModelEmpty)
    var height = 0.dp
    LocalConfiguration.current.screenHeightDp.let { height = (it / 2).dp }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    scope.launch {
        delay(2000)
        sheetState.show()
    }

    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
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
                LoginHead()
                LoginContent(loginModel)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ButtonCustom(
                        text = stringResource(R.string.login),
                        primary = true,
                        enable = true,
                        error = false
                    ) {

                    }
                }
            }
        }
    ) {
    }

}

@Composable
private fun LoginHead() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "¿No tiene cuenta? ",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(top = 4.dp).clickable { },
            text = "Crea una nueva cuenta",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    loginModel: LoginModel
) {
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

    }
}

@ExperimentalMaterial3Api
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
            OutlinedTextFieldCustom(
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

@ExperimentalMaterial3Api
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
            OutlinedTextFieldCustom(
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
