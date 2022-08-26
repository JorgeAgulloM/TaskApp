package com.softyorch.taskapp.ui.components.sliderCustom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.ui.widgets.RowInfo

@Composable
fun sliderCustom(
    initValue: Int,
    enable: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..5f,
    steps: Int = 4,
    onValueChangeFinished: () -> Unit,
    text: String
): Int {

    var selection by rememberSaveable { mutableStateOf(initValue.toFloat()) }
    var size by rememberSaveable { mutableStateOf(selection)}
    val viewModel = hiltViewModel<SliderCustomViewModel>()
    val textSizes = viewModel.sizeSelectedOfUser()

    Column(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            RowInfo(
                text = text,
                paddingStart = 8.dp,
                textSizes = textSizes
            )
            Slider(
                value = selection,
                onValueChange = { selection = it },
                modifier = Modifier.height(16.dp),
                enabled = enable,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = {
                    size = selection
                    onValueChangeFinished.invoke()
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    disabledThumbColor = MaterialTheme.colorScheme.tertiary.copy(0.5f),
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                    activeTickColor = MaterialTheme.colorScheme.secondary,
                    inactiveTickColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
                )
            ).let {
                size
            }
        }
    )

    return size.toInt()
}

