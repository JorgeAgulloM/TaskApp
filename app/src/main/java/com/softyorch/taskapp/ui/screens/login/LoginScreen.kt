package com.softyorch.taskapp.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.textFieldCustom
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.utils.StandardizedSizes
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel = hiltViewModel<LoginViewModel>()
    val textSizes = viewModel.sizeSelectedOfUser()

    Box {
        LoginOrNewAccount(
            modifier = Modifier.fillMaxWidth().align(alignment = Alignment.Center),
            viewModel = viewModel,
            textSizes = textSizes,
            context = context,
            navController = navController
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun LoginOrNewAccount(
    modifier: Modifier,
    viewModel: LoginViewModel,
    textSizes: StandardizedSizes,
    context: Context,
    navController: NavHostController
) {
    var newAccount by rememberSaveable { mutableStateOf(value = false) }
    val name: String by viewModel.name.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val emailRepeat: String by viewModel.emailRepeat.observeAsState(initial = "")
    val pass: String by viewModel.pass.observeAsState(initial = "")
    val passRepeat: String by viewModel.passRepeat.observeAsState(initial = "")
    val rememberMe: Boolean by viewModel.rememberMe.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val newAccountEnable: Boolean by viewModel.newAccountEnable.observeAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        CircularIndicatorCustom(text = if (!newAccount) "...login" else "...loading")

    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleLogin(modifier = modifier, textSizes = textSizes)
            Spacer(modifier = modifier.padding(vertical = 16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                if (newAccount) TextFieldName(name = name) {
                    viewModel.onNewAccountChange(
                        name = it.trim(), email = email, emailRepeat = emailRepeat, pass = pass,
                        passRepeat = passRepeat
                    )
                }

                TextFieldEmail(email = email) {
                    viewModel.onLoginChange(
                        email = it.trim(),
                        pass = pass,
                        rememberMe = rememberMe
                    )
                }

                if (newAccount) TextFieldEmailRepeat(email = emailRepeat) {
                    viewModel.onNewAccountChange(
                        name = name, email = email, emailRepeat = it.trim(), pass = pass,
                        passRepeat = passRepeat
                    )
                }

                TextFieldPass(pass = pass) {
                    viewModel.onLoginChange(
                        email = email,
                        pass = it.trim(),
                        rememberMe = rememberMe
                    )
                }

                if (newAccount) TextFieldPassRepeat(passRepeat = passRepeat) {
                    viewModel.onNewAccountChange(
                        name = name, email = email, emailRepeat = emailRepeat, pass = pass,
                        passRepeat = it.trim()
                    )
                }

                if (!newAccount) CheckerRememberMe(rememberMe = rememberMe) {
                    viewModel.onLoginChange(
                        email = email, pass = pass, rememberMe = it
                    )
                }

                Spacer(modifier = modifier.padding(vertical = 8.dp))
            }
            Spacer(modifier = modifier.padding(vertical = 16.dp))

            ButtonLogin(
                text = if (!newAccount) "Login" else "Create Account",
                enable = if (!newAccount) loginEnable else newAccountEnable,
                primary = true
            ) {
                coroutineScope.launch {
                    if (!newAccount) {
                        if (viewModel.onLoginSelected()) navController.navigate(
                            AppScreensRoutes.MainScreen.route
                        ) {
                            popUpTo(AppScreensRoutes.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        if (viewModel.onNewAccountSelected()) {
                            newAccount = false
                        } else {
                            Toast.makeText(
                                context,
                                "Error, ya existe una cuenta con el correo introducido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            ButtonLogin(
                text = if (!newAccount) "New Account" else "Go to Login"
            ) {
                newAccount = !newAccount
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CheckerRememberMe(rememberMe: Boolean, onCheckedChange: (Boolean) -> Unit) {
    CheckCustom(
        checked = rememberMe,
        onCheckedChange = onCheckedChange,
        text = "Remember Me",
        onClick = {},
        horizontalArrangement = Arrangement.End
    )
}


@Composable
private fun TitleLogin(modifier: Modifier, textSizes: StandardizedSizes) {
    Spacer(modifier = modifier.padding(vertical = 16.dp))
    Image(
        painter = painterResource(id = R.drawable.notes_512x512),
        contentDescription = "app image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(150.dp)
    )
}

/*@Composable
private fun SubTitleLogin(modifier: Modifier, textSizes: StandardizedSizes) {
    RowInfo(
        text = "Please, enter the following data",
        horizontalArrangement = Arrangement.Center,
        textSizes = textSizes
    )
}*/

@Composable
private fun TextFieldName(name: String, onTextFieldChanged: (String) -> Unit) {
    textFieldCustom(
        text = name,
        label = "Name",
        placeholder = "type your name",
        icon = Icons.Rounded.Person,
        contentDescription = "type your name",
        singleLine = true,
        onTextFieldChanged = onTextFieldChanged
        //isError = email.isEmpty() && pushCreate
    )
}

@Composable
private fun TextFieldEmail(email: String, onTextFieldChanged: (String) -> Unit) {
    textFieldCustom(
        text = email,
        label = "Email",
        placeholder = "type your email",
        icon = Icons.Rounded.Email,
        contentDescription = "type your email",
        singleLine = true,
        onTextFieldChanged = onTextFieldChanged,
        //isError = email.isEmpty() && pushCreate
    )
}

@Composable
private fun TextFieldEmailRepeat(email: String, onTextFieldChanged: (String) -> Unit) {
    textFieldCustom(
        text = email,
        label = "Email",
        placeholder = "repeat your email",
        icon = Icons.Rounded.Email,
        contentDescription = "repeat your email",
        singleLine = true,
        onTextFieldChanged = onTextFieldChanged,
        //isError = email.isEmpty() && pushCreate
    )
}

@Composable
private fun TextFieldPass(pass: String, onTextFieldChanged: (String) -> Unit) {
    textFieldCustom(
        text = pass,
        label = "Password",
        placeholder = "type your password",
        icon = Icons.Rounded.Key,
        contentDescription = "type your password",
        singleLine = true,
        onTextFieldChanged = onTextFieldChanged,
        //isError = email.isEmpty() && pushCreate
    )
}

@Composable
private fun TextFieldPassRepeat(passRepeat: String, onTextFieldChanged: (String) -> Unit) {
    textFieldCustom(
        text = passRepeat,
        label = "Password",
        placeholder = "repeat your password",
        icon = Icons.Rounded.Key,
        contentDescription = "repeat your password",
        singleLine = true,
        onTextFieldChanged = onTextFieldChanged,
        //isError = email.isEmpty() && pushCreate
    )
}

@Composable
private fun ButtonLogin(
    text: String, enable: Boolean = true, primary: Boolean = false, onClickSelect: () -> Unit
) {
    ButtonCustom(
        onClick = { onClickSelect() },
        text = text,
        primary = primary,
        enable = enable
    )
}