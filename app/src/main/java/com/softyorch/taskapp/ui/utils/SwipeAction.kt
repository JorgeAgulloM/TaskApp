/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

class SwipeAction(
    val icon: @Composable () -> Unit,
    val background: Color,
    val weight: Double = 1.0,
    val isUndo: Boolean = false,
    val onSwipe: () -> Unit
) {
    init {
        require(weight > 0.0) { "invalid weight; must be grater than zero" }
    }

    fun copy(
        icon: @Composable () -> Unit,
        background: Color = this.background,
        weight: Double = this.weight,
        isUndo: Boolean = this.isUndo,
        onSwipe: () -> Unit = this.onSwipe
    ) = SwipeAction(
        icon = icon,
        background = background,
        weight = weight,
        isUndo = isUndo,
        onSwipe = onSwipe
    )

    fun SwipeAction(
        icon: ImageVector,
        background: Color,
        weight: Double = 1.0,
        isUndo: Boolean = false,
        onSwipe: () -> Unit
    ) = SwipeAction(
        icon = {
            Image(
                modifier = Modifier.padding(16.dp),
                imageVector = icon,
                contentDescription = null
            )
        },
        background = background,
        weight = weight,
        isUndo = isUndo,
        onSwipe = onSwipe
    )

}