package com.softyorch.taskapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CheckCustom(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    animated: Boolean = false,
    text: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: () -> Unit
) {

    var clickOnTask by remember { mutableStateOf(value = false) }
    val slideCheckBox = animateIntOffsetAsState(
        targetValue = if (clickOnTask && animated) IntOffset(60, 0)
        else IntOffset(0, 0),
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 200,
            easing = FastOutLinearInEasing
        )
    )
    val alphaTask: Float by animateFloatAsState(
        targetValue = if (clickOnTask && animated) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 200,
            easing = FastOutLinearInEasing
        ),
        finishedListener = { onClick.invoke() }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(end = 8.dp)
            .height(35.dp)
            .graphicsLayer(alpha = alphaTask)
            .offset {
                slideCheckBox.value
            }
            .clickable {
                clickOnTask = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        /*var checkedChange by remember { mutableStateOf(value = false) }
        var checkedOrNot by remember { mutableStateOf(value = checked) }
        val alphaCheck: Float by animateFloatAsState(
            targetValue = if (checkedChange && animated) 0.2f else 1f,
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = 200,
                easing = FastOutLinearInEasing
            ),
            finishedListener = {
                onCheckedChange.invoke(checkedOrNot)
            }
        )*/
        Checkbox(
            //modifier = Modifier.graphicsLayer(alpha = alphaCheck),
            checked = checked,//checkedOrNot,
            onCheckedChange = {onCheckedChange(it) },/*
                checkedOrNot = !checkedOrNot
                checkedChange = true
            },*/
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
            )
            /* uncheckedColor = MaterialTheme.colorScheme.secondary,
             checkmarkColor = MaterialTheme.colorScheme.secondary
         )*/
        )
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = if (checked) MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline,
                textDecoration = TextDecoration.LineThrough
            ) else MaterialTheme.typography.bodyMedium
        )
    }
}