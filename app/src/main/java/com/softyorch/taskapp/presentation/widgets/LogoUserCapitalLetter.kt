package com.softyorch.taskapp.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun LogoUserCapitalLetter(capitalLetter: String) {
    Text(
        modifier = Modifier.size(30.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            ),
        text = capitalLetter,
        style = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.tertiaryContainer
    )
}