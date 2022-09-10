package com.softyorch.taskapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SnackBarCustom(
    text: String,
    onAction: () -> Unit
) {
    var showSnackBar by remember { mutableStateOf(true) }

    if (showSnackBar) Snackbar(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large,
        dismissAction = {
            IconButton(
                onClick = {
                    onAction()
                    showSnackBar = false
                },
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "close")
            }
        }
    ) {
        Text(text = text)
    }
}

@Preview(name = "SnackBarCustom", showBackground = true)
@Composable
private fun ShowSnackBarCustom() {
    SnackBarCustom(text = "Error al cargar los datos") {}
}