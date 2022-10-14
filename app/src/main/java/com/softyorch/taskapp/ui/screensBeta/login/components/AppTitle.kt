package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R

@Composable
fun AppTitle() {
    val colorGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    )
    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                brush = colorGradient,
                shape = MaterialTheme.shapes.large
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge.copy(
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}