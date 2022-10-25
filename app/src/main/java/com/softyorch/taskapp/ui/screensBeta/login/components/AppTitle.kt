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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R

@Composable
fun AppTitle() {
    val colorGradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 1f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.primary.copy(alpha = 1f),
        )
    )

    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                brush = colorGradient,
                shape = MaterialTheme.shapes.extraLarge
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            ),
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}