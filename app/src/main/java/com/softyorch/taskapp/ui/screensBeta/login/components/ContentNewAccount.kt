package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screensBeta.login.LoginViewModelBeta
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

@Composable
fun ContentNewAccount(viewModel: LoginViewModelBeta) {
    val newAccountModel by viewModel.newAccountModel.observeAsState(initial = NewAccountModel.newAccountModel)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldName(newAccountModel.userName, false, false, false) {}
        TextFieldEmail(newAccountModel.userEmail, false, false, false) {

        }
        TextFieldEmailRepeat(newAccountModel.userEmail, false, false, false) {

        }
        TextFieldPass(newAccountModel.userPass, true, keyboardActions = KeyboardActions(
            onGo = {
                //if (!newAccount) goOrErrorLogin = true
            }
        ), false, false) {}
        TextFieldPassRepeat(newAccountModel.userPass, false, keyboardActions = KeyboardActions(
            onGo = {
                //if (!newAccount) goOrErrorLogin = true
            }
        ), false, false) {}

    }
}