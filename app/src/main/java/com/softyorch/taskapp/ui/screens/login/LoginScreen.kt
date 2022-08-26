package com.softyorch.taskapp.ui.screens.login

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.textFieldCustom
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<LoginViewModel>()
    val textSizes = viewModel.sizeSelectedOfUser()

    val context = LocalContext.current
    var loginOrNewAccount by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        RowInfo(
            text = "TASK APP",
            horizontalArrangement = Arrangement.Center,
            textSizes = textSizes
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
            RowInfo(
                text = "New Account",
                horizontalArrangement = Arrangement.Center,
                textSizes = textSizes
            )

        loginOrNewAccount = if (!loginOrNewAccount)
            loginContent(
                viewModel = viewModel,
                navController = navController,
                context = context
            )
        else
            newAccountContent(
                viewModel = viewModel,
                context = context
            )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
private fun loginContent(
    viewModel: LoginViewModel,
    navController: NavController,
    context: Context
): Boolean {

    val coroutineScope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf(value = "") }
    var pass by rememberSaveable { mutableStateOf(value = "") }
    var rememberMe by rememberSaveable { mutableStateOf(value = false) }

    var newAccount by rememberSaveable { mutableStateOf(value = false) }
    var pushCreate by rememberSaveable { mutableStateOf(value = false) }
    var goToMain by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        name = textFieldCustom(
            text = name,
            label = "Name",
            placeholder = "type your name",
            icon = Icons.Rounded.Person,
            contentDescription = "type your name",
            singleLine = true,
            isError = name.isEmpty() && pushCreate
        )
        pass = textFieldCustom(
            text = pass,
            label = "Password",
            placeholder = "type your password",
            icon = Icons.Rounded.Key,
            contentDescription = "type your password",
            singleLine = true,
            isError = pass.isEmpty() && pushCreate,
            password = true,
        )
        CheckCustom(
            checked = rememberMe,
            onCheckedChange = {
                rememberMe = !rememberMe
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
        ButtonCustom(
            onClick = {
                pushCreate = true
                if (name.isNotEmpty() && pass.isNotEmpty()) {
                    goToMain = true
                }
            },
            text = "Login",
            primary = true
        )
        ButtonCustom(
            onClick = {
                newAccount = true
            },
            text = "New Account"
        )
    }

    if (goToMain) {
        coroutineScope.launch {
            goToMain = false
            pushCreate = false

            if (viewModel.loginUserIntent(name = name, password = pass, rememberMe = rememberMe)) {
                navController.navigate(AppScreensRoutes.MainScreen.route){
                    popUpTo(AppScreensRoutes.LoginScreen.route) {
                        inclusive = true
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Incorrect User or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    return newAccount
}

@Composable
private fun newAccountContent(
    viewModel: LoginViewModel,
    context: Context
): Boolean {

    var name by rememberSaveable { mutableStateOf(value = "") }
    var email by rememberSaveable { mutableStateOf(value = "") }
    var emailOk by remember { mutableStateOf(value = false) }
    var pass by rememberSaveable { mutableStateOf(value = "") }
    var passOk by remember { mutableStateOf(value = false) }
    var image by rememberSaveable { mutableStateOf(value = "") }

    var login by rememberSaveable { mutableStateOf(value = true) }
    var pushCreate by rememberSaveable { mutableStateOf(value = false) }
    var createNewUser by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        name = textFieldCustom(
            text = name,
            label = "Name",
            placeholder = "type your name",
            icon = Icons.Rounded.Person,
            contentDescription = "type your name",
            singleLine = true,
            isError = name.isEmpty() && pushCreate
        )
        email = textFieldCustom(
            text = email,
            label = "Email",
            placeholder = "type your email",
            icon = Icons.Rounded.Email,
            contentDescription = "type your email",
            singleLine = true,
            isError = email.isEmpty() && pushCreate
        )
        emailOk = email == textFieldCustom(
            text = email,
            label = "Confirm Email",
            placeholder = "Confirm email",
            icon = Icons.Rounded.Email,
            contentDescription = "type your email",
            singleLine = true,
            isError = (!emailOk || email.isEmpty()) && pushCreate
        )
        pass = textFieldCustom(
            text = pass,
            label = "Password",
            placeholder = "Type your password",
            icon = Icons.Rounded.Key,
            contentDescription = "type your password",
            singleLine = true,
            isError = pass.isEmpty() && pushCreate,
            password = true,
        )
        passOk = pass == textFieldCustom(
            text = pass,
            label = "Confirm password",
            placeholder = "Confirm password",
            icon = Icons.Rounded.Key,
            contentDescription = "Confirm your password",
            singleLine = true,
            isError = ((!passOk || pass.isEmpty()) && pushCreate),
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
        ButtonCustom(
            onClick = {
                pushCreate = true

                if (name.isNotEmpty() && emailOk && passOk) {
                    createNewUser = true
                }
                /*navController.popBackStack()
                navController.navigate(AppScreensRoutes.MainScreen.route)*/
            },
            text = "Create Account",
            primary = true
        )
        ButtonCustom(
            onClick = {
                login = false
            },
            text = "Go to Login"
        )
    }

    if (createNewUser) {

        val newUser = UserData(
            username = name,
            userEmail = email,
            userPass = pass
        )

        viewModel.userDataList.collectAsState().value.contains(newUser).let {
            if (!it) {
                viewModel.addUser(
                    userData = newUser
                ).let {
                    Toast.makeText(
                        context,
                        "User created",
                        Toast.LENGTH_SHORT
                    ).show().let {
                        createNewUser = false
                        pushCreate = false

                        //Switch the screen to login mode
                        login = false
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Error, user already exist",
                    Toast.LENGTH_SHORT
                ).show().let {
                    createNewUser = false
                    pushCreate = false
                }
            }
        }
    }






    return login
}
