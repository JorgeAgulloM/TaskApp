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
import com.softyorch.taskapp.ui.screens.login.model.AccountModel

@Composable
fun ContentNewAccount(
    accountModel: AccountModel,
    errors: ErrorAccountModel,
    onGo: () -> Unit,
    onNewAccountDataChange: (AccountModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldName(accountModel.userName, errors.name) {
            onNewAccountDataChange(accountModel.copy(userName = it.trim().replaceFirstChar {
                char -> char.uppercase()
            }))
        }
        TextFieldEmail(
            accountModel.userEmail,
            error = errors.email,
            errorAccount = errors.emailExists
        ) {
            onNewAccountDataChange(accountModel.copy(userEmail = it.trim().lowercase()))
        }
        TextFieldEmailRepeat(accountModel.userEmailRepeat, errors.emailRepeat) {
            onNewAccountDataChange(accountModel.copy(userEmailRepeat = it.trim().lowercase()))
        }
        TextFieldPass(
            accountModel.userPass,
            newAccount = true,
            errors.pass,
            keyboardActions = KeyboardActions(onGo = { onGo() })
        ) {
            onNewAccountDataChange(accountModel.copy(userPass = it.trim()))
        }
        TextFieldPassRepeat(
            accountModel.userPassRepeat,
            errors.passRepeat,
            keyboardActions = KeyboardActions(onGo = { onGo() })
        ) {
            onNewAccountDataChange(accountModel.copy(userPassRepeat = it.trim()))
        }

    }
}