package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.*

@Composable
fun ButtonCustom(
    text: String,
    primary: Boolean = false,
    tertiary: Boolean = false,
    enable: Boolean = true,
    error: Boolean = false,
    onClick: () -> Unit
) {

    var onClickButton by remember { mutableStateOf(value = false) }

    val containerColor by error.containerColorAsStateAnimation(
        actionButton = onClickButton, primary = primary, secondary = tertiary
    ){
        if (onClickButton) {
            onClick()
            onClickButton = false
        }
    }

    val contentColor by error.contentColorAsStateAnimation(
        actionButton = primary, primary = tertiary
    )

    Button(
        onClick = {
            onClickButton = true
        },
        modifier = Modifier.width(144.dp).height(40.dp).padding(4.dp),
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        content = {
            Text(
                text = text,
                style = TextStyle(
                    textDecoration = if (primary || tertiary) TextDecoration.None
                    else TextDecoration.Underline
                ),
            )
        },
        contentPadding = PaddingValues(2.dp),
        elevation = ButtonDefaults.buttonElevation(if (primary || tertiary) ELEVATION_DP else 0.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ShowButtonCustom() {

    val error by remember { mutableStateOf(false) }

    ButtonCustom(
        text = "Salvar",
        enable = true,
        primary = true,
        error = error,
        onClick = { error != error },
    )
}