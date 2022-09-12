package com.softyorch.taskapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R

@Composable
fun IconError(
    errorText: String
) {
    Row {
        Icon(
            imageVector = Icons.Rounded.Error,
            contentDescription = stringResource(R.string.error),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = errorText, style = MaterialTheme.typography.labelSmall)
    }
    Spacer(modifier = Modifier.padding(2.dp))
}