package com.softyorch.taskapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val loginViewModel = hiltViewModel<LoginViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Login",
                icon = Icons.Rounded.Login,
                nameScreen = AppScreens.LoginScreen.name,
                navController = navController,
            )
        },
        content = {
            LoginContent(it = it, navController = navController, viewModel = loginViewModel)
        })
}

@Composable
fun LoginContent(it: PaddingValues, navController: NavController, viewModel: LoginViewModel) {

    var name by rememberSaveable { mutableStateOf(value = "") }
    var pass by rememberSaveable { mutableStateOf(value = "") }
    var rememberMe by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = it.calculateTopPadding() * 1.5f,
                bottom = it.calculateTopPadding() * 2f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
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

        Column(
            modifier = Modifier
                .padding(top = 32.dp)
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
                .padding(top = 32.dp)
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

                },
                text = "New Account"
            )
        }

    }
}