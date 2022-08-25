package com.softyorch.taskapp.components.textCustom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun TextCustom(
    text: String,
    description: Boolean = false
) {

    val viewModel = hiltViewModel<TextCustomViewModel>()
    val textSizes = viewModel.sizeSelectedOfUser()

    Text(
        modifier = Modifier.padding(4.dp),
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = textSizes.littleSize,
        fontWeight = if (description) FontWeight.SemiBold else FontWeight.Normal
    )
}