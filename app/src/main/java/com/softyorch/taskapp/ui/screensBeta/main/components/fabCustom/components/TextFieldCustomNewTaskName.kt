/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.IconError
import com.softyorch.taskapp.ui.components.textFieldCustomNewTask

@ExperimentalMaterial3Api
@Composable
fun TextFieldCustomNewTaskName(
    text: String,
    error: Boolean,
    titleDeedCounter: Int,
    limitCharTittle: Int,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask(
            text = text,
            label = stringResource(R.string.name_task),
            placeholder = stringResource(R.string.write_name),
            singleLine = true,
            newTask = true,
            isError = error,
            onTextFieldChanged = onCheckedChange
        )
        if (titleDeedCounter > 15) Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Chars $titleDeedCounter/$limitCharTittle",
                style = MaterialTheme.typography.labelSmall,
                color =
                if (titleDeedCounter < limitCharTittle) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
        if (error) IconError(errorText = stringResource(R.string.text_input_min_little))
    }
}