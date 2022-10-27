/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.userData

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.softyorch.taskapp.R

@Composable
fun UserDataDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            ButtonCustomDataScreen(text = stringResource(R.string.cancel)) { onDismissButtonClick() }
        },
        confirmButton = {
            ButtonCustomDataScreen(text = confirmButtonText, primary = true) {
                onConfirmButtonClick()
            }
        },
        title = { androidx.compose.material3.Text(text = title) },
        text = {
            androidx.compose.material3.Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(textAlign = TextAlign.Center)
            )
        },
        icon = {}
    )
}