/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.utils


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.*


@Composable
fun rememberSwipeableActionsState(): SwipeableActionsState {
    return remember { SwipeableActionsState() }
}

/**
 * The state of a [SwipeableActionsBox].
 */
@Stable
class SwipeableActionsState internal constructor() {

    internal var offsetState = mutableStateOf(0f)
    val offset: State<Float> get() = offsetState

    /**
     * The current position (in pixels) of a [SwipeableActionsBox].
     */
    var isResettingOnRelease: Boolean by mutableStateOf(false)
        private set

    internal lateinit var canSwipeTowardsRight: () -> Boolean
    internal lateinit var canSwipeTowardsLeft: () -> Boolean

    internal val draggableState = DraggableState { delta ->
        val targetOffset = offsetState.value + delta
        val isAllowed = isResettingOnRelease
                || targetOffset > 0f && canSwipeTowardsRight()
                || targetOffset < 0f && canSwipeTowardsLeft()

        // Add some resistance if needed
        offsetState.value += if (isAllowed) delta else delta / 10
    }

    internal suspend fun resetOffset() {
        draggableState.drag(MutatePriority.PreventUserInput) {
            isResettingOnRelease = true
            try {
                Animatable(offsetState.value).animateTo(
                    targetValue = 0f,
                    tween(durationMillis = 400)
                ) {
                    dragBy(value - offsetState.value)
                }
            } finally {
                isResettingOnRelease = false
            }
        }
    }
}