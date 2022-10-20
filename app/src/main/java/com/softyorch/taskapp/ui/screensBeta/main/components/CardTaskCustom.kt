/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.DetailsTask
import com.softyorch.taskapp.ui.theme.AppPrimaryLight
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CardTaskCustom(
    task: TaskModelUi,
    isVisible: Boolean,
    onCheckedChange: (Boolean) -> Unit
){

    var isOpen by remember { mutableStateOf(false) }
    val scrollString = rememberScrollState()
    val scope = rememberCoroutineScope()
    if (!isOpen) scope.launch {
        scrollString.animateScrollTo(
            value = 0,
            animationSpec = tween(400, 0, LinearOutSlowInEasing)
        )
    }

    val lines = 3
    val maxTextLength = 45
    val minCollapse = 95
    val lineHeight = 20

    var isMinCollapse by remember { mutableStateOf(value = false) }
    val expandSize = ((task.description.length / maxTextLength) * lineHeight) + 120
    val collapseSize: Int
    if (task.description.length / maxTextLength > (lines * lineHeight)) {
        collapseSize = minCollapse
        isMinCollapse = true
    } else {
        collapseSize = expandSize
        isMinCollapse = false
    }

    if (!isVisible) isOpen = false
    val height by isOpen.upDownIntegerAnimated(expandSize, collapseSize)
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .heightIn(max = height.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = AppPrimaryLight
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = ELEVATION_DP
        )
    ){
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    isOpen = !isOpen
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollString, enabled = isOpen)
                    .padding(start = 4.dp, bottom = 4.dp)
            ) {
                DetailsTask(task, isOpen) {
                    isOpen = false
                    onCheckedChange(it)
                }
                Text(
                    text = if (isOpen) task.description else
                        if (isMinCollapse) task.description.substring(0, maxTextLength) + "..."
                        else task.description,
                    lineHeight = lineHeight.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}