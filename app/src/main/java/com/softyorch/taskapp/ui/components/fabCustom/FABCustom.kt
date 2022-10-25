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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.outlinedTextFieldCustom
import com.softyorch.taskapp.ui.widgets.newTask.TextFieldCustomNewTaskDescription
import com.softyorch.taskapp.ui.widgets.newTask.TextFieldCustomNewTaskName
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import com.softyorch.taskapp.utils.ELEVATION_DP
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
            .width(width.dp)
            .shadow(
                ELEVATION_DP,
                shape = MaterialTheme.shapes.large,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            ),
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
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                TextFieldCustomNewTaskName(
                    text = "",
                    error = false,
                    titleDeedCounter = 35,
                    limitCharTittle = 40
                ){

                }
                TextFieldCustomNewTaskDescription(
                    text = "",
                    keyboardActions = KeyboardActions(),
                    error = false
                ){

                }

            }
            //Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonCustom(text = "Save", true) {
                    openDialog = false
                }
                ButtonCustom(text = "Close") {
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
