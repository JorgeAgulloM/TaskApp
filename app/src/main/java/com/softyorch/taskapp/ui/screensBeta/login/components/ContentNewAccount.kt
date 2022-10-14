package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

@Composable
fun ContentNewAccount(
    newAccountModel: NewAccountModel,
    errors: ErrorNewAccountModel,
    onGo: (Boolean) -> Unit,
    onNewAccountDataChange: (NewAccountModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldName(newAccountModel.userName, errors.name) {
            onNewAccountDataChange(newAccountModel.copy(userName = it.trim().replaceFirstChar {
                char -> char.uppercase()
            }))
        }
        TextFieldEmail(
            newAccountModel.userEmail,
            error = errors.email,
            errorAccount = errors.emailExists
        ) {
            onNewAccountDataChange(newAccountModel.copy(userEmail = it.trim().lowercase()))
        }
        TextFieldEmailRepeat(newAccountModel.userEmailRepeat, errors.emailRepeat) {
            onNewAccountDataChange(newAccountModel.copy(userEmailRepeat = it.trim().lowercase()))
        }
        TextFieldPass(
            newAccountModel.userPass,
            newAccount = true,
            errors.pass,
            keyboardActions = KeyboardActions(onGo = { onGo(true) })
        ) {
            onNewAccountDataChange(newAccountModel.copy(userPass = it.trim()))
        }
        TextFieldPassRepeat(
            newAccountModel.userPassRepeat,
            errors.passRepeat,
            keyboardActions = KeyboardActions(onGo = { onGo(true) })
        ) {
            onNewAccountDataChange(newAccountModel.copy(userPassRepeat = it.trim()))
        }

    }
}