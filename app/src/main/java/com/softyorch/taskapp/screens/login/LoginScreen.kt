package com.softyorch.taskapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.*

@Composable
fun LoginScreen(navController: NavHostController) {

    val loginViewModel = hiltViewModel<LoginViewModel>()
    var loginOrNewAccount by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        RowIndication(
            text = "TASK APP",
            fontSize = 24.sp,
            horizontalArrangement = Arrangement.Center
        )
        if (!loginOrNewAccount)
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Image of user",
                modifier = Modifier
                    .size(194.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.medium.copy(
                            CornerSize(25.dp)
                        )
                    )
            )
        else
            RowIndication(
                text = "New Account",
                fontSize = 16.sp,
                horizontalArrangement = Arrangement.Center
            )

        loginOrNewAccount = if (!loginOrNewAccount)
            loginContent(
                viewModel = loginViewModel,
                navController = navController
            )
        else
            newAccountContent(
                viewModel = loginViewModel,
                navController = navController
            )
    }
}

@Composable
private fun loginContent(viewModel: LoginViewModel, navController: NavController): Boolean {

    var name by rememberSaveable { mutableStateOf(value = "") }
    var pass by rememberSaveable { mutableStateOf(value = "") }
    var rememberMe by rememberSaveable { mutableStateOf(value = false) }

    var newAccount by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        name = textFieldTask(
            text = name,
            label = "Name",
            placeholder = "type your name",
            icon = Icons.Rounded.Person,
            contentDescription = "type your name",
            singleLine = true
        )
        pass = textFieldTask(
            text = pass,
            label = "Password",
            placeholder = "type your password",
            icon = Icons.Rounded.Key,
            contentDescription = "type your password",
            singleLine = true,
            password = true,
        )
        TaskSummaryCheck(
            checked = false,
            onCheckedChange = {
                rememberMe = it
            },
            text = "Remember Me",
            onClick = {},
            horizontalArrangement = Arrangement.End
        )
    }

    Column(
        modifier = Modifier
            .padding(bottom = 64.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaskButton(
            onClick = {
                navController.popBackStack()
                navController.navigate(AppScreensRoutes.MainScreen.route)
            },
            text = "Login",
            primary = true
        )
        TaskButton(
            onClick = {
                newAccount = true
            },
            text = "New Account"
        )
    }
    return newAccount
}

@Composable
private fun newAccountContent(viewModel: LoginViewModel, navController: NavController): Boolean {

    var name by rememberSaveable { mutableStateOf(value = "") }
    var email by rememberSaveable { mutableStateOf(value = "") }
    var emailOk by remember { mutableStateOf(value = false) }
    var pass by rememberSaveable { mutableStateOf(value = "") }
    var passOk by remember { mutableStateOf(value = false) }
    var image by rememberSaveable { mutableStateOf(value = "") }

    var login by rememberSaveable { mutableStateOf(value = true) }
    var pushCreate by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        name = textFieldTask(
            text = name,
            label = "Name",
            placeholder = "type your name",
            icon = Icons.Rounded.Person,
            contentDescription = "type your name",
            singleLine = true
        )
        email = textFieldTask(
            text = email,
            label = "Email",
            placeholder = "type your email",
            icon = Icons.Rounded.Email,
            contentDescription = "type your email",
            singleLine = true
        )
        emailOk = email == textFieldTask(
            text = email,
            label = "Confirm Email",
            placeholder = "Confirm email",
            icon = Icons.Rounded.Email,
            contentDescription = "type your email",
            singleLine = true,
            isError = !emailOk && pushCreate
        )
        pass = textFieldTask(
            text = pass,
            label = "Password",
            placeholder = "Type your password",
            icon = Icons.Rounded.Key,
            contentDescription = "type your password",
            singleLine = true,
            password = true,
        )
        passOk = pass == textFieldTask(
            text = pass,
            label = "Confirm password",
            placeholder = "Confirm password",
            icon = Icons.Rounded.Key,
            contentDescription = "Confirm your password",
            singleLine = true,
            isError = (!passOk && pushCreate),
            password = true,
        )
    }

    Column(
        modifier = Modifier
            .padding(bottom = 64.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaskButton(
            onClick = {
                pushCreate = true

                /*navController.popBackStack()
                navController.navigate(AppScreensRoutes.MainScreen.route)*/
            },
            text = "Create Account",
            primary = true
        )
        TaskButton(
            onClick = {
                login = false
            },
            text = "Go to Login"
        )
        Text(text = pushCreate.toString())
    }
    return login
}
