package com.softyorch.taskapp.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_DP

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LogoUserCapitalLetter(capitalLetter: String, size: Dp, function: () -> Unit) {

    val padding = 16.dp
    val weight = size + (padding * 2)

    Box(
        modifier = Modifier
            .size(weight, size)
            .padding(horizontal = padding)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            ).clickable {
                function()
            },
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = capitalLetter,
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}