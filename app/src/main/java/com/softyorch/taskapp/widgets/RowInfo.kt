package com.softyorch.taskapp.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.StandardizedSizes

const val elevationF: Float = 4f

@Composable
fun RowInfo(
    text: String,
    textSizes: StandardizedSizes,
    paddingStart: Dp = 0.dp,
    heightSize: Dp = 30.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightSize)
            .padding(start = paddingStart, top = 0.dp, bottom = 0.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = textSizes.littleSize,
            style = TextStyle(
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.primary,
                    offset = Offset(
                        x = elevationF,
                        y = elevationF
                    ),
                    blurRadius = elevationF
                )
            )
        )

    }
}