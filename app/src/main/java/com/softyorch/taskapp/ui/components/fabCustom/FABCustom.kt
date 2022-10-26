package com.softyorch.taskapp.ui.components.fabCustom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.widgets.newTask.ShowTaskNewTask
import com.softyorch.taskapp.ui.widgets.newTask.TextFieldCustomNewTaskDescription
import com.softyorch.taskapp.ui.widgets.newTask.TextFieldCustomNewTaskName
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.extensions.toStringFormatDate
import java.time.Instant
import java.util.*


@ExperimentalMaterial3Api
@Composable
fun FABCustom(onOpen: (Boolean) -> Unit) {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")
    var openDialog by remember { mutableStateOf(false) }

    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 10) * 9
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val calculateWidth = maxWidth - 32
    val focusManager = LocalFocusManager.current

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
        ),
        finishedListener = {
            if (openDialog) focusManager.moveFocus(FocusDirection.Up)
        }
    )

    if (!openDialog) focusManager.clearFocus()

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
            ShowTaskNewTask(
                userName = userName, dateFormatted = Date.from(Instant.now()).toStringFormatDate()
            )
            //Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextFieldCustomNewTaskName( //añadir focus a este field
                    text = "",
                    error = false,
                    titleDeedCounter = 35,
                    limitCharTittle = 40
                ) {

                }
                TextFieldCustomNewTaskDescription(
                    text = "",
                    keyboardActions = KeyboardActions(),
                    error = false
                ) {

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
                    onOpen(false)
                }
                ButtonCustom(text = "Close") {
                    openDialog = false
                    onOpen(false)
                }
            }
        }

        AnimatedVisibility(
            visible = !openDialog
        ){
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(add_task),
                        color = if (openDialog) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(add_task),
                        tint = if (openDialog) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                shape = MaterialTheme.shapes.large,
                containerColor = if (openDialog) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.primaryContainer,
                onClick = {
                    openDialog = true
                    onOpen(true)
                }
            )
        }


    }

}
