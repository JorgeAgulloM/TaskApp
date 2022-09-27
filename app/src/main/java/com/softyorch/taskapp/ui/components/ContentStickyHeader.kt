package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun ContentStickyHeader(published: String) {
    Text(
        text = "Publicación $published",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 2.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large
            ),
        style = MaterialTheme.typography.titleSmall
            .copy(color = MaterialTheme.colorScheme.secondary),
        textAlign = TextAlign.Center
    )
}