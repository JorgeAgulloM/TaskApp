/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.utils.*

@Composable
fun CardTaskCustom(
    task: TaskModelUi,
    isVisible: Boolean,
    onCheckedChange: (TaskModelUi) -> Unit
) {

    var isOpen by remember { mutableStateOf(false) }

    val lines = 3
    val maxTextLength = 45
    val lineHeight = 20

    var isMinCollapse by remember { mutableStateOf(value = false) }

    isMinCollapse = task.description.length / maxTextLength > lines

    if (!isVisible) isOpen = false

    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(3.dp),
            topEnd = CornerSize(50.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp)
        ),
        /*colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),*/
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = ELEVATION_DP
        )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    isOpen = !isOpen
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            DetailsTask(isMinCollapse, maxTextLength, lineHeight, task, isOpen) {
                isOpen = false
                onCheckedChange(it)
            }
        }
    }

}
