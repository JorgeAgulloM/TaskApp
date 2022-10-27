/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.models.AccountModel
import com.softyorch.taskapp.utils.emptyString

@Composable
fun UserDataDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            ButtonCustomDataScreen(text = stringResource(cancel)) { onDismissButtonClick() }
        },
        confirmButton = {
            ButtonCustomDataScreen(text = confirmButtonText, primary = true) {
                onConfirmButtonClick()
            }
        },
        title = { Text(text = title) },
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(textAlign = TextAlign.Center)
            )
        },
        icon = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataConfirmDialog(
    userData: AccountModel,
    errorEmailRepeat: Boolean,
    errorPassRepeat: Boolean,
    onDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: (AccountModel) -> Unit
) {
    val userConfirm by remember { mutableStateOf(value = userData) }
    var emailConfirm by remember { mutableStateOf(value = userData.userEmail) }
    var passConfirm by remember { mutableStateOf(value = emptyString) }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            ButtonCustomDataScreen(text = stringResource(cancel)) { onDismissButtonClick() }
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = stringResource(content_confirm_data),
                tint = Color.Yellow
            )
        },
        title = { Text(text = stringResource(text_confim_data)) },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextFieldCustomDataScreen(
                    text = emailConfirm,
                    label = stringResource(repeat_your_email),
                    icon = Icons.Rounded.Email,
                    keyboardType = KeyboardType.Email,
                    error = errorEmailRepeat,
                    errorText = stringResource(input_error_repeat_email),
                    onTextFieldChanged = {
                        emailConfirm = it.trim()
                    }
                )
                TextFieldCustomDataScreen(
                    text = passConfirm,
                    label = stringResource(repeat_your_password),
                    icon = Icons.Rounded.Key,
                    keyboardType = KeyboardType.Password,
                    password = true,
                    error = errorPassRepeat,
                    errorEmailExist = false,
                    errorText = stringResource(input_error_pass),
                    onTextFieldChanged = {
                        passConfirm = it.trim()
                    }
                )
            }
        },
        confirmButton = {
            ButtonCustomDataScreen(text = stringResource(confirm), primary = true) {
                onConfirmButtonClick(
                    userConfirm.copy(
                        userEmailRepeat = emailConfirm,
                        userPassRepeat = passConfirm
                    )
                )
            }
        }
    )
}