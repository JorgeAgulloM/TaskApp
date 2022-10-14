package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel

@Composable
fun ContentLogin(
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
                    userEmail = it.trim().lowercase(),
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