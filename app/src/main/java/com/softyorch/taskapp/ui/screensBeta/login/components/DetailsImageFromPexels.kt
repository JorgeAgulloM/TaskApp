package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.extensions.contentColorLabelAsStateAnimation

@Composable
fun DetailsImageFromPexels(
    text: String,
    onClick: () -> Unit
) {
    var click by remember { mutableStateOf(value = false) }
    val clickColor by click.contentColorLabelAsStateAnimation {
        if (click) {
            onClick()
            click = false
        }
    }
    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            clickColor.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    )

    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                brush = colorGradient,
                shape = MaterialTheme.shapes.small
            ).clickable {
                click = true
            },
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}