package com.softyorch.taskapp.presentation.components.switchCustom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SwitchCustom(
    text: String,
    checked: Boolean = false,
    enable: Boolean = true,
    onCheckedChange: () -> Unit
) {

    var stateSwitch by rememberSaveable { mutableStateOf(checked) }

    Row(
        modifier = Modifier.padding(start = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Switch(
            checked = stateSwitch,
            onCheckedChange = {
                stateSwitch = !stateSwitch
                onCheckedChange.invoke()
            },
            thumbContent = {
                if (stateSwitch)
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = text,
                    )
            },
            enabled = enable,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedIconColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.secondary
            )
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}