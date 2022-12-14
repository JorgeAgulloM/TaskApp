package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

@Composable
fun SwitchCustom(
    text: String,
    checked: Boolean = false,
    enable: Boolean = true,
    description: String,
    onCheckedChange: () -> Unit
) {

    var stateSwitch by rememberSaveable { mutableStateOf(checked) }

    Row(
        modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
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
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = description,
                style = MaterialTheme.typography.labelSmall
            )

        }
    }
}