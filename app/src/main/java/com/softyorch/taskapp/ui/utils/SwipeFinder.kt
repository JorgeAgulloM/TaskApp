/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.utils

import androidx.compose.ui.unit.IntOffset
import java.lang.Math.abs

internal data class SwipeActionMeta(
    val value: SwipeAction,
    val isOnRightSide: Boolean
)

internal data class ActionFinder(
    private val left: List<SwipeAction>,
    private val right: List<SwipeAction>
) {

    fun actionAt(offset: Float, totalWidth: Int): SwipeActionMeta? {
        if (offset == 0f) {
            return null
        }

        val isOnRightSide = offset < 0f
        val actions = if (isOnRightSide) right else left

        val actionAtOffset = actions.actionAt(
            offset = abs(offset).coerceAtMost(totalWidth.toFloat()),
            totalWidth = totalWidth
        )
        return actionAtOffset?.let {
            SwipeActionMeta(
                value = actionAtOffset,
                isOnRightSide = isOnRightSide
            )
        }
    }

    private fun List<SwipeAction>.actionAt(offset: Float, totalWidth: Int): SwipeAction? {
        if (isEmpty()) {
            return null
        }

        val totalWeights = this.sumOf { it.weight }
        var offsetSoFar = 0.0

        for (index in 0 until size) {
            val action = this[index]
            val actionWidth = (action.weight / totalWeights) * totalWidth
            val actionEndX = offsetSoFar + actionWidth

            if (offset <= actionEndX) return action

            offsetSoFar += actionEndX
        }

        error("Coludn't find any swipe action. Width=$totalWidth, offset=$offset, actions=$this")
    }
}