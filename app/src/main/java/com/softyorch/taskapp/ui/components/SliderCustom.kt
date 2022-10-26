package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.widgets.RowInfo

@Composable
fun sliderCustom(
    initValue: Int,
    enable: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..5f,
    steps: Int = 4,
    text: String,
    style: TextStyle = MaterialTheme.typography.labelSmall,
    onValueChangeFinished: (Int) -> Unit
): Int {

    var selection by rememberSaveable { mutableStateOf(initValue.toFloat()) }

    Column(
        modifier = Modifier
            .padding(start = 34.dp, end = 32.dp, top = 16.dp, bottom = 8.dp)
            .height(56.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            RowInfo(text = text, style = style)
            Slider(
                value = selection,
                onValueChange = { selection = it },
                enabled = enable,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = {
                    onValueChangeFinished.invoke(selection.toInt())
                }
            )
        }
    )
    return selection.toInt()
}

