package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.intOffsetAnimation

@ExperimentalMaterial3Api
@Composable
fun CheckCustom(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    animated: Boolean = false,
    text: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: () -> Unit
) {

    var clickOnTask by remember { mutableStateOf(value = false) }
    val slideCheckBox by animated.intOffsetAnimation(stateOne = clickOnTask)

    Row(
        modifier = Modifier
            .padding(end = 8.dp)
            .height(35.dp)
            .offset {
                slideCheckBox
            }
            .clickable {
                onClick()
                clickOnTask = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = {

                onCheckedChange(it)
            },
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.onTertiary
            )
        )
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = if (checked) MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline,
                textDecoration = TextDecoration.LineThrough
            ) else MaterialTheme.typography.bodyMedium
        )
    }
}