/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.ShowTaskDetails
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTask(
    isMinCollapse: Boolean,
    maxTextLength: Int,
    lineHeight: Int,
    task: TaskModelUi,
    isOpen: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val scrollString = rememberScrollState()
    val scope = rememberCoroutineScope()
    if (!isOpen) scope.launch {
        scrollString.animateScrollTo(
            value = 0,
            animationSpec = tween(400, 0, LinearOutSlowInEasing)
        )
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = SHEET_TRANSITION_ENTER,
        exit = SHEET_TRANSITION_EXIT
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowTaskDetails(task)
            DividerCustom(16.dp, 4.dp)
        }
    }
    CheckCustom(
        checked = task.checkState,
        onCheckedChange = {
            onCheckedChange(it)
        },
        enabled = true,
        animated = true,
        text = task.title,
    ) {}
    Column(
        modifier = Modifier
            .padding(start = 4.dp, bottom = 4.dp)
    ) {
        Text(
            text = textTransform(isOpen, task.description, isMinCollapse, maxTextLength),
            lineHeight = lineHeight.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(4.dp)
        )
    }
}

private fun textTransform(
    isOpen: Boolean,
    description: String,
    isMinCollapse: Boolean,
    maxTextLength: Int
) = if (isOpen) {
    description
} else if (isMinCollapse && description.length > maxTextLength) {
    description.substring(0, maxTextLength) + "..."
} else description

