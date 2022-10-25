package com.softyorch.taskapp.ui.components.fabCustom

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.outlinedTextFieldCustom
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM

@ExperimentalMaterial3Api
@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")

    var openDialog by remember { mutableStateOf(false) }


    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 5) * 3
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val calculateWidth = maxWidth - 32

    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    val height by animateIntAsState(
        targetValue = if (openDialog) calculateHeight else 55,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    val width by animateIntAsState(
        targetValue = if (openDialog) calculateWidth else 156,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .height(height.dp)
            .width(width.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .height(height.dp)
                .width(width.dp)
                .background(brush = sheetBrush, shape = MaterialTheme.shapes.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //Head

            //Body
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                outlinedTextFieldCustom(
                    text = "",
                    label = stringResource(name_task),
                    placeholder = stringResource(write_name),
                    icon = Icons.Rounded.Title,
                    contentDescription = stringResource(write_name),
                    keyboardOptions = KEYBOARD_OPTIONS_CUSTOM,
                    keyboardActions = KeyboardActions(),
                    isError = false,
                    isVisible = true,
                    password = false,
                    onTextFieldChanged = {

                    }
                )
                outlinedTextFieldCustom(
                    text = "",
                    label = stringResource(task_description),
                    placeholder = stringResource(write_description),
                    icon = Icons.Rounded.Description,
                    contentDescription = stringResource(write_description),
                    keyboardOptions = KEYBOARD_OPTIONS_CUSTOM,
                    keyboardActions = KeyboardActions(),
                    isError = false,
                    isVisible = true,
                    password = false,
                    onTextFieldChanged = {

                    }
                )
            }
            //Footer
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonCustom(text = "Save", true) {
                    openDialog = false
                }
                ButtonCustom(text = "Close", true) {
                    openDialog = false
                }
            }
        }

        ExtendedFloatingActionButton(
            text = {
                Text(text = stringResource(add_task))
            },
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(add_task)
                )
            },
            shape = MaterialTheme.shapes.large,
            onClick = { openDialog = true }
        )
    }


}
