package com.softyorch.taskapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextCustom(
    text: String,
    description: Boolean = false
) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 14.sp,
        fontWeight = if (description) FontWeight.SemiBold else FontWeight.Normal
    )
}