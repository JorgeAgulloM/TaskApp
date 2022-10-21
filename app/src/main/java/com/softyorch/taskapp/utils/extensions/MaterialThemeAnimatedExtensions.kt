package com.softyorch.taskapp.utils.extensions

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

@Composable
fun MaterialTheme.infiniteTransitionAnimateColor(): State<Color> =
    rememberInfiniteTransition().animateColor(
        initialValue = this.colorScheme.tertiary,
        targetValue = this.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )