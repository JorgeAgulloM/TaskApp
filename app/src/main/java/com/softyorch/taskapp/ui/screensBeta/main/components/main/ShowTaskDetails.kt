/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.components.common.ShowTask
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.extensions.toStringFormatted

@Composable
fun ShowTaskDetails(task: TaskModelUi) {
    ShowTask(
        author = task.author,
        date = task.entryDate.toStringFormatted(),
        completedDate = task.finishDate?.toStringFormatted()
            ?: emptyString,
        paddingStart = 8.dp
    )
}