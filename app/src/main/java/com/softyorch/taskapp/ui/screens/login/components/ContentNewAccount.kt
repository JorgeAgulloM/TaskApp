package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import com.softyorch.taskapp.ui.models.UserModelUi

@Composable
fun ContentNewAccount(
    userModelUi: UserModelUi,
    errors: ErrorAccountModel,
    onGo: () -> Unit,
    onNewAccountDataChange: (UserModelUi) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldName(userModelUi.userName, errors.name) {
            onNewAccountDataChange(userModelUi.copy(userName = it.trim().replaceFirstChar {
                char -> char.uppercase()
            }))
        }
        TextFieldEmail(
            userModelUi.userEmail,
            error = errors.email,
            errorAccount = errors.emailExists
        ) {
            onNewAccountDataChange(userModelUi.copy(userEmail = it.trim().lowercase()))
        }
        TextFieldEmailRepeat(userModelUi.userEmailRepeat, errors.emailRepeat) {
            onNewAccountDataChange(userModelUi.copy(userEmailRepeat = it.trim().lowercase()))
        }
        TextFieldPass(
            userModelUi.userPass,
            newAccount = true,
            errors.pass,
            keyboardActions = KeyboardActions(onGo = { onGo() })
        ) {
            onNewAccountDataChange(userModelUi.copy(userPass = it.trim()))
        }
        TextFieldPassRepeat(
            userModelUi.userPassRepeat,
            errors.passRepeat,
            keyboardActions = KeyboardActions(onGo = { onGo() })
        ) {
            onNewAccountDataChange(userModelUi.copy(userPassRepeat = it.trim()))
        }

    }
}