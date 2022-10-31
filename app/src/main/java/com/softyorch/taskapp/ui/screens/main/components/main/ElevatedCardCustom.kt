/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.utils.*
import com.softyorch.taskapp.ui.utils.SwipeRippleState
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

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

    val squareSize = (LocalConfiguration.current.screenWidthDp / 2).dp

    val swipeableState = rememberSwipeableState(0)

    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    val drop = SwipeAction(
        icon = { Icons.Rounded.Delete },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {

        }
    )
    val edit = SwipeAction(
        icon = { Icons.Rounded.Edit },
        background = MaterialTheme.colorScheme.primary,
        onSwipe = {

        }
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
                color = MaterialTheme.colorScheme.surfaceVariant,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .height(40.dp)
                    .width(squareSize / 3),
                tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .height(40.dp)
                    .width(squareSize / 3)
                    .clickable {
                        deleteTask(task)
                    },
                tint = MaterialTheme.colorScheme.error
            )
        }
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
                                            if (swipeableState.offset.value > 0) {
                                                swipeableState.animateTo(
                                                    targetValue = 0,
                                                    anim = tween(durationMillis = 300)
                                                )
                                            }
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

@Composable
private fun SwipeableActionBox(
    modifier: Modifier = Modifier,
    state: SwipeableActionsState = rememberSwipeableActionsState(),
    startActions: List<SwipeAction> = emptyList(),
    endActions: List<SwipeAction> = emptyList(),
    swipeThreshold: Dp = 40.dp,
    backgroundUntilSwipeThreshold: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val leftActions = if (isRtl) endActions else startActions
    val rightActions = if (isRtl) startActions else startActions
    val swipeThresholdPx = LocalDensity.current.run { swipeThreshold.toPx() }

    val ripple = remember {
        SwipeRippleState()
    }

    val actions = remember(leftActions, rightActions) {
        ActionFinder(left = leftActions, right = rightActions)
    }
    LaunchedEffect(state, actions) {
        state.run {
            canSwipeTowardsRight = { leftActions.isNotEmpty() }
            canSwipeTowardsLeft = { rightActions.isNotEmpty() }
        }
    }

    val offset = state.offset.value
    val thresholdCrossed = abs(offset) > swipeThresholdPx

    var swipedAction: SwipeActionMeta? by remember {
        mutableStateOf(value = null)
    }
    val visibleAction: SwipeActionMeta? = remember(offset, actions) {
        actions.actionAt(offset, totalWidth = constraints.maxWidth)
    }

    val backgroundColor: Color by animateColorAsState(
        when {
            swipedAction != null -> swipedAction!!.value.background
            !thresholdCrossed -> backgroundUntilSwipeThreshold
            visibleAction == null -> Color.Transparent
            else -> visibleAction.value.background
        }
    )

    Box(
        modifier = Modifier
            .absoluteOffset { IntOffset(x = offset.roundToInt(), y = 0) }
            .drawOverContent { ripple.draw(scope = this) }
            .draggable(
                orientation = Orientation.Horizontal,
                enabled = !state.isResettingOnRelease,
                onDragStopped = {
                    if (thresholdCrossed && visibleAction != null) {
                        swipedAction = visibleAction
                        swipedAction!!.value.onSwipe()
                        ripple.animate(
                            action = swipedAction!!,
                            scope = this
                        )
                    }
                    launch {
                        state.resetOffset()
                        swipedAction = null
                    }
                },
                state = state.draggableState,
            ),
        content = content
    )

    (swipedAction ?: visibleAction)?.let { action ->
        ActionIconBox(
            modifier = Modifier.matchParentSize(),
            action = action,
            offset = offset,
            backgroundColor = backgroundColor,
            content = { action.value.icon() }
        )
    }

}

@Composable
private fun ActionIconBox(
    action: SwipeActionMeta,
    offset: Float,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .layout { measurable, constraints ->
                val placeholder = measurable.measure(constraints)
                layout(width = placeholder.width, height = placeholder.height) {
                    val iconOffset =
                        if (action.isOnRightSide) constraints.maxWidth + offset else offset - placeholder.width
                    placeholder.placeRelative(x = iconOffset.roundToInt(), y = 0)
                }
            }
            .background(color = backgroundColor),
        horizontalArrangement = if (action.isOnRightSide) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        content()
    }
}

private fun Modifier.drawOverContent(onDraw: DrawScope.() -> Unit) =
    drawWithContent {
        drawContent()
        onDraw(this)
    }
