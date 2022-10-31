/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.theme.AppPrimaryDark
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ElevatedCardCustom(
    task: TaskModelUi,
    scope: CoroutineScope,
    deleteTask: (TaskModelUi) -> Unit,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    isChecked = task.checkState

    val swipeableState = rememberSwipeableState(0)
    val squareSize = 160.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1, sizePx to 2)

    var openDeleteDialog by remember { mutableStateOf(value = false) }





    //Revisar que esto funciona correctamente...
    if (swipeableState.offset.value < -340) {
        if (!openDeleteDialog) openDeleteDialog = true
    } else if (swipeableState.offset.value > 340) {
        Log.d("Swipe", "Edit") /**Falta aplicar la edición*/
    }
    //Revisar que esto funciona correctamente...





    if (openDeleteDialog) DialogDeleteTask {
        openDeleteDialog = false
        scope.launch {
            swipeableState.animateTo(
                targetValue = 0,
                anim = tween(durationMillis = 300)
            )
            if (it) deleteTask(task)
        }
    }

    ElevatedSwipebleCard(swipeableState, anchors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isOpen = !isOpen
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            content = {
                AnimatedShowTaskDetails(isOpen, task)
                CheckBoxCard(isChecked, scope, swipeableState, task) {
                    isChecked = it
                    scope.launch {
                        if (swipeableState.offset.value > 0) {
                            swipeableState.animateTo(
                                targetValue = 0,
                                anim = tween(durationMillis = 300)
                            )
                        }
                        delay(300)
                        onCheckedChange(
                            task.copy(
                                checkState = it,
                                finishDate = if (it) Date.from(
                                    Instant.now()
                                ) else null
                            )
                        )
                    }
                }
                FoldingText(task, isOpen)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ElevatedSwipebleCard(
    swipeableState: SwipeableState<Int>,
    anchors: Map<Float, Int>,
    scopeCard: @Composable () -> Unit
) {

    val topColor = animateColorAsState(
        targetValue =
        if (swipeableState.offset.value < -340) {
            MaterialTheme.colorScheme.error
        } else if (swipeableState.offset.value > 340) {
            AppPrimaryDark.copy(alpha = 0.4f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = spring(dampingRatio = 3f),
    )

    val bottomColor = animateColorAsState(
        targetValue = if (swipeableState.offset.value < -340) {
            MaterialTheme.colorScheme.errorContainer
        } else if (swipeableState.offset.value > 340) {
            AppPrimaryDark
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = spring(dampingRatio = 3f)
    )

    val bgBrush = Brush.horizontalGradient(
        colors = listOf(
            topColor.value,
            bottomColor.value
        )
    )


    Box(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 25.dp,
                top = 4.dp,
                bottom = 16.dp
            )
            .fillMaxWidth()
            .background(
                brush = bgBrush,
                shape = MaterialTheme.shapes.large.copy(
                    topStart = CornerSize(3.dp),
                    topEnd = CornerSize(50.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp)
                )
            )
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.8f) },
                orientation = Orientation.Horizontal
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        RowSwipeOptions()
        ElevatedCard(
            modifier = Modifier.offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
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
                scopeCard()
            }
        )
    }
}

@Composable
private fun ColumnScope.AnimatedShowTaskDetails(
    isOpen: Boolean,
    task: TaskModelUi
) {
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
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CheckBoxCard(
    isChecked: Boolean,
    scope: CoroutineScope,
    swipeableState: SwipeableState<Int>,
    task: TaskModelUi,
    onCheckedChange: (Boolean) -> Unit
) {
    var state by remember { mutableStateOf(value = false) }
    state = isChecked
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
                checked = state,
                onCheckedChange = {
                    onCheckedChange(it)
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
}

@Composable
private fun FoldingText(
    task: TaskModelUi,
    isOpen: Boolean
) {
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

@Composable
private fun RowSwipeOptions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .height(40.dp)
                .size(50.dp)
        )
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 24.dp)
                .height(40.dp)
                .size(50.dp)
        )
    }
}

@Composable
private fun DialogDeleteTask(dialogScope: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {
            dialogScope(false)
        },
        dismissButton = {
            ButtonCustom(
                onClick = {
                    dialogScope(false)
                },
                text = stringResource(R.string.cancel)
            )
        },
        confirmButton = {
            ButtonCustom(
                onClick = {
                    dialogScope(true)
                },
                text = stringResource(R.string.delete),
                primary = true
            )
        },
        text = { Text(text = stringResource(R.string.you_sure_eliminate_task)) }
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