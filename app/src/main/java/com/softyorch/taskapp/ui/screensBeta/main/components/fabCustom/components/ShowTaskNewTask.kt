/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screensBeta.main.components.common.ShowTask

@Composable
fun ShowTaskNewTask(
    userName: String,
    dateFormatted: String
) {
    Column {
        ShowTask(
            author = userName,
            date = dateFormatted
        )
        Divider(modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp))
    }

}