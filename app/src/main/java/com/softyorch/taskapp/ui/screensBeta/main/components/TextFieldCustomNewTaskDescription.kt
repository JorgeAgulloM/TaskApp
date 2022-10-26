/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.IconError
import com.softyorch.taskapp.ui.components.textFieldCustomNewTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@ExperimentalMaterial3Api
@Composable
fun TextFieldCustomNewTaskDescription(
    text: String,
    keyboardActions: KeyboardActions,
    error: Boolean,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask(
            text = text,
            label = stringResource(R.string.task_description),
            placeholder = stringResource(R.string.write_description),
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                imeAction = ImeAction.Go
            ),
            keyboardActions = keyboardActions,
            newTask = true,
            isError = error,
            onTextFieldChanged = onCheckedChange
        )
        if (error) IconError(errorText = stringResource(R.string.text_input_min_little))
    }
}