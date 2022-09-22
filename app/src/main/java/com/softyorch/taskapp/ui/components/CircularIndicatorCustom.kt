package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.softyorch.taskapp.utils.infiniteTransitionAnimateColor

@Composable
fun CircularIndicatorCustomDialog(
    text: String,
    modifier: Modifier = Modifier
){
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
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.infiniteTransitionAnimateColor().value
        )
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }

}

