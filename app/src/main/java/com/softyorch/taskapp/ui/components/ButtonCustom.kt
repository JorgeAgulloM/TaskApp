package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.theme.DarkMode90t
import com.softyorch.taskapp.ui.widgets.elevationDp
import com.softyorch.taskapp.ui.widgets.elevationF

@Composable
fun ButtonCustom(
    onClick: () -> Unit,
    text: String,
    primary: Boolean = false,
    enable: Boolean = true

) {

    //TODO

    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.width(144.dp).height(40.dp).padding(2.dp),
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (primary) MaterialTheme.colorScheme.tertiary else Color.Transparent,
            contentColor = if (primary) DarkMode90t else MaterialTheme.colorScheme.onSurface
        ),
        content = {
            Text(
                text = text,
                style = TextStyle(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.primary,
                        offset = if (primary) Offset(x = 0f, y = 0f) else Offset(
                            x = elevationF,
                            y = elevationF
                        ),
                        blurRadius = if (primary) 0f else elevationF
                    )
                ),
                textDecoration = if (!primary) TextDecoration.Underline else null
            )
        },
        contentPadding = PaddingValues(2.dp),
        elevation = ButtonDefaults.buttonElevation(if (primary) elevationDp else 0.dp)
    )
}