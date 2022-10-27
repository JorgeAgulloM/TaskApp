/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.IconError
import com.softyorch.taskapp.ui.components.textFieldCustomInputData
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@ExperimentalMaterial3Api
@Composable
fun TextFieldCustomDataScreen(
    text: String,
    label: String,
    icon: ImageVector,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    password: Boolean = false,
    error: Boolean,
    errorEmailExist: Boolean = false,
    errorText: String,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.height(TextFieldDefaults.MinHeight + 8.dp)) {
            textFieldCustomInputData(
                text = text,
                label = label,
                placeholder = stringResource(R.string.write_your_label) + label.lowercase(),
                icon = icon,
                contentDescription = label + stringResource(R.string.label_of_user),
                keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                    capitalization = capitalization,
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                singleLine = true,
                isError = error || errorEmailExist,
                password = password,
                onTextFieldChanged = { onTextFieldChanged(it) }
            )
        }
        if (error || errorEmailExist) IconError(
            errorText =
            if (errorEmailExist) stringResource(R.string.error_email_exist)
            else errorText
        )

    }
}