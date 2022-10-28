package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.IconError
import com.softyorch.taskapp.ui.components.textFieldCustomInputData
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldEmail(
    email: String,
    error: Boolean,
    errorAccount: Boolean = false,
    errorEmailExist: Boolean = false,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            textFieldCustomInputData(
                text = email,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.type_your_email),
                icon = Icons.Rounded.Email,
                contentDescription = stringResource(R.string.type_your_email),
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email
                ),
                isError = error || errorAccount || errorEmailExist,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error && !errorAccount) IconError(
            errorText = if (errorEmailExist) stringResource(R.string.error_email_exist)
            else stringResource(R.string.input_error_email)
        )
    }
}