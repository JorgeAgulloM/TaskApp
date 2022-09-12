package com.softyorch.taskapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_FLOAT
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun ButtonCustom(
    text: String,
    primary: Boolean = false,
    tertiary: Boolean = false,
    enable: Boolean = true,
    error: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.width(144.dp).height(40.dp).padding(4.dp),
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor =
            if (error) MaterialTheme.colorScheme.error
            else if (primary) MaterialTheme.colorScheme.primary
            else if (tertiary) MaterialTheme.colorScheme.tertiary
            else Color.Transparent,
            contentColor =
            if (error) MaterialTheme.colorScheme.onError
            else if (primary) MaterialTheme.colorScheme.background
            else if (tertiary) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.onBackground
        ),
        content = {
            Text(
                text = text,
                style = TextStyle(
                    shadow = Shadow(
                        //color = MaterialTheme.colorScheme.primary,
                        offset = if (primary || tertiary) Offset(x = 0f, y = 0f) else Offset(
                            x = ELEVATION_FLOAT,
                            y = ELEVATION_FLOAT
                        ),
                        blurRadius = if (primary || tertiary) 0f else ELEVATION_FLOAT
                    )
                ),
            )
        },
        contentPadding = PaddingValues(2.dp),
        elevation = ButtonDefaults.buttonElevation(if (primary || tertiary) ELEVATION_DP else 0.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun showButtonCustom() {

    val error by remember { mutableStateOf(false) }

    ButtonCustom(
        text = "Salvar",
        enable = true,
        primary = true,
        error = error,
        onClick = { error != error},
    )
}