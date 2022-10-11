package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.extensions.infiniteTransitionAnimateColor


@Composable
fun CircularIndicatorCustomDialog(
    text: String,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        CircularIndicatorCustom(text = text, modifier = modifier)
    }
}

@Composable
fun CircularIndicatorCustom(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .size(45.dp)
                .shadow(elevation = ELEVATION_DP * 2, shape = CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.infiniteTransitionAnimateColor().value
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surface
        )
    }

}

