package com.softyorch.taskapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CheckCustom(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    text: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(1f).padding(end = 8.dp).height(35.dp).clickable {
            onClick.invoke()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        content = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                )
                /* uncheckedColor = MaterialTheme.colorScheme.secondary,
                 checkmarkColor = MaterialTheme.colorScheme.secondary
             )*/
            )
            Text(
                text = text,
                /*color =
                if (checked)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,*/
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = if (checked) MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.outline,
                    textDecoration = TextDecoration.LineThrough
                ) else MaterialTheme.typography.bodyMedium
            )
        }
    )
}