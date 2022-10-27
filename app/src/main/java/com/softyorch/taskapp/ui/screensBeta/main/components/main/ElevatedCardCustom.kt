/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@Composable
fun ElevatedCardCustom(
    task: TaskModelUi,
    scope: CoroutineScope,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    isChecked = task.checkState

    ElevatedCard(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 25.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
            .shadow(
                ELEVATION_DP,
                shape = MaterialTheme.shapes.large.copy(
                    topStart = CornerSize(3.dp),
                    topEnd = CornerSize(50.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp)
                ),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        shape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(3.dp),
            topEnd = CornerSize(50.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp)
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = ELEVATION_DP
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        isOpen = !isOpen
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {
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
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                (-8).dp, (-8).dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        content = {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { click ->
                                    scope.launch {
                                        isChecked = click
                                        delay(300)
                                        onCheckedChange(
                                            task.copy(
                                                checkState = click,
                                                finishDate = if (click) Date.from(
                                                    Instant.now()
                                                ) else null
                                            )
                                        )
                                    }
                                }
                            )
                            Text(
                                text = task.title,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = if (task.checkState) MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.outline,
                                    textDecoration = TextDecoration.LineThrough,
                                ) else MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )

                    var isMinCollapse by remember {
                        mutableStateOf(
                            value = false
                        )
                    }

                    val lines = 3
                    val maxTextLength = 40
                    val lineHeight = 20
                    isMinCollapse =
                        task.description.length / maxTextLength > lines

                    Text(
                        text = textTransform(
                            isOpen,
                            task.description,
                            isMinCollapse,
                            maxTextLength
                        ),
                        modifier = Modifier
                            .offset(0.dp, (-16).dp)
                            .padding(
                                start = 8.dp,
                                end = 4.dp
                            ),
                        lineHeight = lineHeight.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    )
}

@Composable
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