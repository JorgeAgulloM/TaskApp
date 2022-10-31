package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.IconError
import com.softyorch.taskapp.ui.components.textFieldCustomInputData
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPass(
    pass: String,
    newAccount: Boolean = false,
    error: Boolean,
    errorAccount: Boolean = false,
    keyboardActions: KeyboardActions,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            textFieldCustomInputData(
                text = pass,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.type_your_password),
                icon = Icons.Rounded.Key,
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (newAccount) ImeAction.Next else ImeAction.Go
                ),
                keyboardActions = keyboardActions,
                contentDescription = stringResource(R.string.type_your_password),
                isError = error || errorAccount,
                password = true,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error) IconError(
            errorText = if (errorAccount) stringResource(R.string.error_email_or_pass)
            else stringResource(R.string.input_error_pass)
        )
    }
}