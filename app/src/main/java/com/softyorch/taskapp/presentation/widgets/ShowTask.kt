package com.softyorch.taskapp.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.components.TextCustom
import com.softyorch.taskapp.utils.emptyString


@Composable
fun ShowTask(
    author: String,
    date: String,
    completedDate: String = emptyString,
    paddingStart: Dp = 24.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = paddingStart),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom(stringResource(created_by), description = true)
            TextCustom(stringResource(created_date), description = true)
            TextCustom(stringResource(completed_date), description = true)
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom(text = author)
            TextCustom(text = date)
            TextCustom(text = completedDate)
        }
    }
}



