package com.softyorch.taskapp.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectText
import com.softyorch.taskapp.widgets.RowInfo

@Composable
fun sliderCustom(
    text: String,
    initValue: Int,
    valueRange: ClosedFloatingPointRange<Float> = 0f..6f,
    steps: Int = 5,
    enable: Boolean = true,
    onValueFinished: () -> Unit
): Int {

    var textShow by rememberSaveable { mutableStateOf(text) }
    var selection by rememberSaveable { mutableStateOf(initValue.toFloat()) }

    textShow = timeLimitAutoLoginSelectText(selector = selection.toInt())

    Log.d("Slider", "Value of selection -> $selection")

    Column(
        modifier = Modifier.padding(horizontal = 16.dp).safeContentPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            RowInfo(text = "$text = $textShow", fontSize = 12.sp)
            Slider(
                value = selection,
                onValueChange = { selection = it },
                enabled = enable,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = onValueFinished,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    disabledThumbColor = MaterialTheme.colorScheme.tertiary.copy(0.5f),
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                    activeTickColor = MaterialTheme.colorScheme.secondary,
                    inactiveTickColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
                )
            )
        }
    )



    return selection.toInt()
}

