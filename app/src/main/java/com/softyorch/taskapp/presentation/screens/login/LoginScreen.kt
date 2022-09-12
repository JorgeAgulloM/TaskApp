package com.softyorch.taskapp.presentation.screens.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.ErrorUserInput
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.CheckCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.emptyString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel = hiltViewModel<LoginViewModel>()

    Box {
        LoginOrNewAccount(
            modifier = Modifier.fillMaxWidth().align(alignment = Alignment.Center),
            viewModel = viewModel,
            context = context,
            navController = navController
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
private fun LoginOrNewAccount(
    modifier: Modifier,
    viewModel: LoginViewModel,
    context: Context,
    navController: NavHostController
) {
    var newAccount by rememberSaveable { mutableStateOf(value = false) }
    val name: String by viewModel.name.observeAsState(initial = emptyString)
    val email: String by viewModel.email.observeAsState(initial = emptyString)
    val emailRepeat: String by viewModel.emailRepeat.observeAsState(initial = emptyString)
    val pass: String by viewModel.pass.observeAsState(initial = emptyString)
    val passRepeat: String by viewModel.passRepeat.observeAsState(initial = emptyString)
    val rememberMe: Boolean by viewModel.rememberMe.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val newAccountEnable: Boolean by viewModel.newAccountEnable.observeAsState(initial = false)

    /** Error states */
    val errorName: Boolean by viewModel.errorName.observeAsState(initial = false)
    val errorEmail: Boolean by viewModel.errorEmail.observeAsState(initial = false)
    val errorRepeatEmail: Boolean by viewModel.errorRepeatEmail.observeAsState(initial = false)
    val errorPass: Boolean by viewModel.errorPass.observeAsState(initial = false)
    val errorRepeatPass: Boolean by viewModel.errorRepeatPass.observeAsState(initial = false)
    val error: Boolean by viewModel.error.observeAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()
    var goOrErrorNewAccount by remember { mutableStateOf(value = false) }
    var goOrErrorLogin by remember { mutableStateOf(value = false) }

    val errorEmailAlreadyUsed = stringResource(error_email_already_exists)

    if (isLoading) {
        CircularIndicatorCustom(
            text = if (!newAccount) stringResource(loading_login) else stringResource(
                loading_loading
            )
        )

    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleLogin(modifier = modifier)
            Spacer(modifier = modifier.padding(vertical = 16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                if (newAccount) TextFieldName(name = name, error = errorName) {
                    viewModel.onNewAccountInputChange(
                        name = it.trim(), email = email, emailRepeat = emailRepeat, pass = pass,
                        passRepeat = passRepeat
                    )
                }

                TextFieldEmail(email = email, error = errorEmail) {
                    viewModel.onLoginInputChange(
                        email = it.trim().lowercase(),
                        pass = pass,
                        rememberMe = rememberMe
                    )
                }

                if (newAccount)
                    TextFieldEmailRepeat(email = emailRepeat, error = errorRepeatEmail) {
                        viewModel.onNewAccountInputChange(
                            name = name, email = email, emailRepeat = it.trim().lowercase(),
                            pass = pass, passRepeat = passRepeat
                        )
                    }

                TextFieldPass(pass = pass, newAccount = newAccount,
                    keyboardActions = KeyboardActions(
                        onGo = {
                            /**TODO Tengo que sacar esto de aquí, es código repetido*/
                            if (!newAccount) goOrErrorLogin = true
                        }
                    ),
                    error = errorPass) {
                    Log.d("ERRORS", "pass.it -> $it")
                    viewModel.onLoginInputChange(
                        email = email,
                        pass = it.trim(),
                        rememberMe = rememberMe
                    )
                }

                if (newAccount) TextFieldPassRepeat(
                    passRepeat = passRepeat,
                    keyboardActions = KeyboardActions(
                        onGo = { goOrErrorNewAccount = true }
                    ),
                    error = errorRepeatPass) {
                    viewModel.onNewAccountInputChange(
                        name = name, email = email, emailRepeat = emailRepeat, pass = pass,
                        passRepeat = it.trim()
                    )
                }

                if (!newAccount) CheckerRememberMe(rememberMe = rememberMe) {
                    viewModel.onLoginInputChange(
                        email = email, pass = pass, rememberMe = it
                    )
                }

                Spacer(modifier = modifier.padding(vertical = 8.dp))
            }
            Spacer(modifier = modifier.padding(vertical = 16.dp))

            ButtonLogin(
                text = if (!newAccount) stringResource(login) else stringResource(create_account),
                enable = if (!newAccount) loginEnable else newAccountEnable,
                primary = true,
                error = error
            ) {
                if (!newAccount) {
                    goOrErrorLogin = true
                } else {
                    goOrErrorNewAccount = true
                }

            }
            ButtonLogin(
                text = if (!newAccount) stringResource(new_account) else stringResource(go_to_login)
            ) {
                newAccount = !newAccount
                viewModel.resetErrorChangeLoginToNewAccountVis()
            }

            var showSnackBarErrors by remember { mutableStateOf(value = false) }
            if (goOrErrorNewAccount) {
                coroutineScope.launch {
                    val errors = viewModel.onNewAccountDataSend(
                        name = name,
                        email = email,
                        emailRepeat = emailRepeat,
                        pass = pass,
                        passRepeat = passRepeat
                    )
                    if (errors.error) {
                        showSnackBarErrors = true
                    } else {
                        newAccount = false
                    }
                    goOrErrorNewAccount = false
                }
            }
            if (showSnackBarErrors) SnackBarMessageLoginOrNewAccount {
                showSnackBarErrors = false
            }
            if (!error) showSnackBarErrors = false

            if (goOrErrorLogin) {
                coroutineScope.launch {
                    if (!viewModel.onLoginDataSend(
                            email = email,
                            pass = pass
                        )
                    ) navController.navigate(
                        AppScreensRoutes.MainScreen.route
                    ) {
                        popUpTo(AppScreensRoutes.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
                goOrErrorLogin = false
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun SnackBarMessageLoginOrNewAccount(
    onDismiss: () -> Unit
) {

    rememberCoroutineScope().launch{
        delay(5000)
        onDismiss()
    }

    Snackbar(
        modifier = Modifier.padding(8.dp).safeContentPadding(),
        shape = MaterialTheme.shapes.large,
        dismissAction = {
            IconButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = stringResource(content_close_snack))
            }
        }
    ) {
        IconError(stringResource(snack_input_error))
    }
}

@Composable
private fun IconError(
    errorText: String
) {
    Row {
        Icon(
            imageVector = Icons.Rounded.Error,
            contentDescription = stringResource(error),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = errorText, style = MaterialTheme.typography.labelSmall)
    }
    Spacer(modifier = Modifier.padding(2.dp))
}

@ExperimentalMaterial3Api
@Composable
private fun CheckerRememberMe(
    rememberMe: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    CheckCustom(
        checked = rememberMe,
        onCheckedChange = onCheckedChange,
        text = stringResource(remember_me),
        horizontalArrangement = Arrangement.End,
        onClick = {}
    )
}

@Composable
private fun TitleLogin(modifier: Modifier) {
    Spacer(modifier = modifier.padding(vertical = 16.dp))
    Image(
        painter = painterResource(id = R.drawable.notes_512x512),
        contentDescription = stringResource(app_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(150.dp)
    )
}

@Composable
private fun TextFieldName(name: String, error: Boolean, onTextFieldChanged: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustom(
            text = name,
            label = stringResource(R.string.name),
            placeholder = stringResource(type_your_name),
            icon = Icons.Rounded.Person,
            contentDescription = stringResource(type_your_name),
            singleLine = true,
            isError = error,
            onTextFieldChanged = onTextFieldChanged
        )
        if (error) IconError(errorText = stringResource(input_error_name))
    }
}

@Composable
private fun TextFieldEmail(
    email: String,
    error: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustom(
            text = email,
            label = stringResource(R.string.email),
            placeholder = stringResource(type_your_email),
            icon = Icons.Rounded.Email,
            contentDescription = stringResource(type_your_email),
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            isError = error,
            onTextFieldChanged = onTextFieldChanged
        )
        if (error) IconError(errorText = stringResource(input_error_email))
    }
}

@Composable
private fun TextFieldEmailRepeat(
    email: String,
    error: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustom(
            text = email,
            label = stringResource(R.string.email),
            placeholder = stringResource(repeat_your_email),
            icon = Icons.Rounded.Email,
            contentDescription = stringResource(repeat_your_email),
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            isError = error,
            onTextFieldChanged = onTextFieldChanged
        )
        if (error) IconError(errorText = stringResource(input_error_repeat_email))
    }
}

@Composable
private fun TextFieldPass(
    pass: String, newAccount: Boolean, keyboardActions: KeyboardActions, error: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustom(
            text = pass,
            label = stringResource(password),
            placeholder = stringResource(type_your_password),
            icon = Icons.Rounded.Key,
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                keyboardType = KeyboardType.Password,
                imeAction = if (newAccount) ImeAction.Next else ImeAction.Go
            ),
            keyboardActions = keyboardActions,
            contentDescription = stringResource(type_your_password),
            singleLine = true,
            isError = error,
            password = true,
            onTextFieldChanged = onTextFieldChanged
        )
        if (error) IconError(errorText = stringResource(input_error_pass))
    }
}

@Composable
private fun TextFieldPassRepeat(
    passRepeat: String, keyboardActions: KeyboardActions, error: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustom(
            text = passRepeat,
            label = stringResource(password),
            placeholder = stringResource(repeat_your_password),
            icon = Icons.Rounded.Key,
            contentDescription = stringResource(repeat_your_password),
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Go
            ),
            keyboardActions = keyboardActions,
            singleLine = true,
            isError = error,
            password = true,
            onTextFieldChanged = onTextFieldChanged
        )
        if (error) IconError(errorText = stringResource(input_error_repeat_pass))
    }
}

@Composable
private fun ButtonLogin(
    text: String,
    primary: Boolean = false,
    enable: Boolean = true,
    error: Boolean = false,
    onClickSelect: () -> Unit
) {
    ButtonCustom(
        text = text,
        primary = primary,
        enable = enable,
        error = error,
        onClick = { onClickSelect() }
    )
}