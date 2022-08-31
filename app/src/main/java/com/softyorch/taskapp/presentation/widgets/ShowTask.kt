package com.softyorch.taskapp.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.presentation.components.TextCustom
import com.softyorch.taskapp.utils.StandardizedSizes


@Composable
fun ShowTask(
    author: String,
    date: String,
    completedDate: String = "",
    textSizes: StandardizedSizes,
    paddingStart: Dp = 24.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = paddingStart),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom("Created By:", textSizes = textSizes.highSize, description = true)
            TextCustom("Created date:", textSizes = textSizes.highSize, description = true)
            TextCustom("Completed date:", textSizes = textSizes.highSize, description = true)
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom(author, textSizes = textSizes.littleSize)
            TextCustom(date, textSizes = textSizes.littleSize)
            TextCustom(completedDate, textSizes = textSizes.littleSize)
        }
    }
}



