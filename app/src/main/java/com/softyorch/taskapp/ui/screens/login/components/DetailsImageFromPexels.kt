package com.softyorch.taskapp.ui.screens.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
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

    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                color = clickColor,
                shape = MaterialTheme.shapes.small
            )
            .clickable {
                click = true
            },
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Cursive,
                textDecoration = TextDecoration.Underline
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}