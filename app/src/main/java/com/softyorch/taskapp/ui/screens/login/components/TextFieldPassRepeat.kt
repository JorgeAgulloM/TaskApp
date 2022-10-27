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
import com.softyorch.taskapp.ui.components.outlinedTextFieldCustom
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPassRepeat(
    pass: String,
    error: Boolean,
    keyboardActions: KeyboardActions,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            outlinedTextFieldCustom(
                text = pass,
                label = stringResource(R.string.repeat_your_password),
                placeholder = stringResource(R.string.repeat_your_password),
                icon = Icons.Rounded.Key,
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = keyboardActions,
                contentDescription = stringResource(R.string.repeat_your_password),
                isError = error,
                password = true,
                onTextFieldChanged = onTextFieldChanged
            )
        }
        if (error) IconError(stringResource(R.string.input_error_repeat_pass))
    }
}