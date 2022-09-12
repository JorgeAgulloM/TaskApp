package com.softyorch.taskapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SnackBarError(
    onDismiss: () -> Unit
) {
    rememberCoroutineScope().launch {
        delay(5000)
        onDismiss()
    }

    Snackbar(
        modifier = Modifier.padding(8.dp).safeContentPadding(),
        shape = MaterialTheme.shapes.large,
        dismissAction = {
            IconButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.content_close_snack)
                )
            }
        }
    ) {
        IconError(stringResource(R.string.snack_input_error))
    }

}

@Preview(name = "SnackBarCustom", showBackground = true)
@Composable
private fun ShowSnackBarCustom() {
    SnackBarError() {}
}