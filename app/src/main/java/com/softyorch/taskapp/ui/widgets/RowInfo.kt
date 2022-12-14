package com.softyorch.taskapp.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowInfo(
    text: String,
    paddingStart: Dp = 0.dp,
    heightSize: Dp = 30.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    style: TextStyle = MaterialTheme.typography.titleMedium
) {
    Row(
        modifier = Modifier
            //.fillMaxWidth(0.9f)
            .height(heightSize)
            .padding(start = paddingStart, top = 0.dp, bottom = 0.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = text,
            style = style
        )
    }
}