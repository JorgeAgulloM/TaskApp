/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.ShowTaskDetails
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailsTask(
    isMinCollapse: Boolean,
    maxTextLength: Int,
    lineHeight: Int,
    task: TaskModelUi,
    isOpen: Boolean,
    onCheckedChange: (TaskModelUi) -> Unit
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

    Row(
        modifier = Modifier.offset((-8).dp, (-8).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = task.checkState,
            onCheckedChange = {
                onCheckedChange(
                    task.copy(
                        checkState = it,
                        finishDate = if (it) Date.from(Instant.now()) else null
                    )
                )
            }
        )
        Text(
            text = task.title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = if (task.checkState) MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.outline,
                textDecoration = TextDecoration.LineThrough
            ) else MaterialTheme.typography.titleSmall
        )
    }

    Column(
        modifier = Modifier
            .offset(0.dp, (-16).dp)
            .padding(start = 8.dp, bottom = 0.dp, end = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = textTransform(isOpen, task.description, isMinCollapse, maxTextLength),
            lineHeight = lineHeight.sp,
            style = if (isOpen) MaterialTheme.typography.bodyMedium
            else MaterialTheme.typography.bodySmall,
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

