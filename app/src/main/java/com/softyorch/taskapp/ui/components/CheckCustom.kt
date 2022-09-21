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
    var clickOnCheck by remember { mutableStateOf(value = false) }
    val slideCheckBox = animateIntOffsetAsState(
        targetValue = if (clickOnTask && animated) IntOffset(60, 0)
        else IntOffset(0, 0),
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100,
            easing = LinearEasing
        )
    )
    val alphaTask: Float by animateFloatAsState(
        targetValue = if ((clickOnTask || clickOnCheck) && animated) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100,
            easing = LinearEasing
        ),
        finishedListener = {
            clickOnCheck = false
            if (clickOnTask) onClick.invoke()
        }
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

        Checkbox(
            checked = checked,
            onCheckedChange = {
                clickOnCheck = true
                onCheckedChange(it)
            },
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.onTertiary
            )
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